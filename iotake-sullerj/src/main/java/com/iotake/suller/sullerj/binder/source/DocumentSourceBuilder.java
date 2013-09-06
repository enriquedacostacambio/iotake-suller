package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.beans.BindingException;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinder;
import com.iotake.suller.sullerj.binder.annotation.SolrEmbeddable;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrName;
import com.iotake.suller.sullerj.binder.annotation.SolrReadable;
import com.iotake.suller.sullerj.binder.annotation.SolrTarget;
import com.iotake.suller.sullerj.binder.annotation.SolrTargetCollection;
import com.iotake.suller.sullerj.binder.annotation.SolrTransient;
import com.iotake.suller.sullerj.binder.annotation.SolrWritable;
import com.iotake.suller.sullerj.binder.collection.CollectionCreator;
import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;
import com.iotake.suller.sullerj.binder.value.EnumValueConverterFactory;
import com.iotake.suller.sullerj.binder.value.GlobalIdValueConverterFactory;
import com.iotake.suller.sullerj.binder.value.MultiSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.SingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.ValueConverter;
import com.iotake.suller.sullerj.binder.value.ValueConverterFactory;

public class DocumentSourceBuilder {

  private final ExtendedDocumentObjectBinder binder;

  public DocumentSourceBuilder(ExtendedDocumentObjectBinder binder) {
    this.binder = binder;
  }

  public RootClassSource create(Class<?> documentClass) {
    BeanInstantiator instantiator = binder.createBeanInstantiator(documentClass);
    FieldSource idFieldSource = findIdSource(documentClass, documentClass);
    FieldSource[] fieldSources = findFieldSources(documentClass,
        idFieldSource.getField(), null, false);
    return new RootClassSource(documentClass, instantiator, idFieldSource,
        fieldSources, binder.getClassFieldName(), binder.getClassesFieldName());

  }

  private FieldSource[] findFieldSources(Class<?> documentClass, Field idField,
      String path, boolean isMultiValued) {
    List<FieldSource> fieldSources = new LinkedList<FieldSource>();
    collectSources(documentClass, documentClass, idField, fieldSources, path,
        isMultiValued);
    return fieldSources.toArray(new FieldSource[fieldSources.size()]);
  }

  private String getPath(Class<?> declaringClass) {
    SolrName solrName = declaringClass.getAnnotation(SolrName.class);
    if (solrName == null) {
      return declaringClass.getSimpleName();
    }
    return solrName.value();
  }

  private PropertySource findIdSource(Class<?> documentClass,
      Class<?> currentClass) {
    PropertySource idSource = doFindIdSource(documentClass, currentClass);
    if (idSource == null) {
      throw new BindingException("Missing id field in Solr document class "
          + documentClass.getName() + ".");
    }
    return idSource;
  }

  private PropertySource doFindIdSource(Class<?> documentClass,
      Class<?> currentClass) {
    PropertySource idSource = null;
    Class<?> superclass = currentClass.getSuperclass();
    if (superclass != null && superclass != Object.class) {
      idSource = findIdSource(documentClass, superclass);
    }
    for (Field field : currentClass.getDeclaredFields()) {
      if (idSource == null) {
        idSource = getIdSource(documentClass, field);
      } else if (getIdSource(documentClass, field) != null) {
        throw new BindingException("Duplicate id field in Solr document class "
            + documentClass.getName() + ": " + currentClass.getName() + "."
            + idSource.getField().getName() + " and " + currentClass.getName()
            + "." + field.getName());
      }
    }
    return idSource;
  }

  private PropertySource getIdSource(Class<?> documentClass, Field field) {
    if (Modifier.isStatic(field.getModifiers())) {
      return null;
    }
    SolrId solrId = field.getAnnotation(SolrId.class);
    if (solrId == null) {
      return null;
    }
    Class<?> fieldType = getFieldType(documentClass, field);
    if (getFieldComponentType(documentClass, field) != null) {
      throw new BindingException("@SolrId field of " + documentClass.getName()
          + " cannot be a collection: " + field.getDeclaringClass().getName()
          + "." + field.getName());
    }
    ValueConverterFactory converterFactory = new GlobalIdValueConverterFactory();
    SingleSlotValueConverter converter = (SingleSlotValueConverter) converterFactory
        .create(fieldType, documentClass, field, binder.getGlobalIdFieldName(),
            binder.getFieldPathSeprator());
    return new SingleSlotNotMultiValuedPropertySource(documentClass, field,
        true, true, converter);
  }

  private void collectSources(Class<?> documentClass, Class<?> currentClass,
      Field idField, List<FieldSource> fieldSources, String path,
      boolean multivalued) {
    collectClassSources(documentClass, currentClass, idField, fieldSources,
        path, multivalued);

    Class<?> superclass = currentClass.getSuperclass();
    if (superclass != null && superclass != Object.class) {
      collectSources(documentClass, superclass, idField, fieldSources, path,
          multivalued);
    }
    for (Class<?> anInterface : currentClass.getInterfaces()) {
      collectSources(documentClass, anInterface, idField, fieldSources, path,
          multivalued);
    }
  }

  private void collectClassSources(Class<?> documentClass,
      Class<?> currentClass, Field idField, List<FieldSource> fieldSources,
      String path, boolean multivalued) {
    for (Field field : currentClass.getDeclaredFields()) {
      boolean isId = field.equals(idField);
      FieldSource fieldSource = getFieldSource(documentClass, field, path,
          multivalued, isId);
      if (fieldSource != null) {
        fieldSources.add(fieldSource);
      }
    }
  }

  private FieldSource getFieldSource(Class<?> documentClass, Field field,
      String path, boolean multivalued, boolean isId) {
    if (Modifier.isStatic(field.getModifiers())) {
      return null;
    }
    Class<?> fieldType = getFieldType(documentClass, field);
    Class<?> componentType = getFieldComponentType(documentClass, field);

    SolrTransient solrTransient = field.getAnnotation(SolrTransient.class);
    if (solrTransient != null) {
      return null;
    }
    String name;
    SolrName solrName = field.getAnnotation(SolrName.class);
    if (solrName == null) {
      name = field.getName();
    } else {
      name = solrName.value();
    }
    boolean readable = isReadable(field, isId);
    boolean writable = isWritable(field, isId);

    String newPath;
    if (path == null) {
      String parentPath = getPath(field.getDeclaringClass());
      if (parentPath == null || parentPath.length() == 0) {
        newPath = name;
      } else {
        newPath = parentPath + binder.getFieldPathSeprator() + name;
      }
    } else {
      newPath = path + binder.getFieldPathSeprator() + name;
    }

    boolean newMultivalued = componentType != null;
    if (newMultivalued && multivalued) {
      throw new BindingException(
          "Nested muti-valued properties are not supported: "
              + field.getDeclaringClass().getName() + "." + field.getName());
    }
    boolean multivaluedNow = multivalued || newMultivalued;

    Class<?> targetType = componentType == null ? fieldType : componentType;
    SolrEmbeddable solrEmbedded = field.getAnnotation(SolrEmbeddable.class);
    if (solrEmbedded == null) {
      solrEmbedded = targetType.getAnnotation(SolrEmbeddable.class);
    }
    com.iotake.suller.sullerj.binder.annotation.SolrDocument solrDocument = targetType
        .getAnnotation(com.iotake.suller.sullerj.binder.annotation.SolrDocument.class);
    FieldSource documentSource = null;
    if (solrEmbedded != null) {
      documentSource = createEmbeddedSource(documentClass, field, readable,
          writable, newPath, multivaluedNow, targetType);
    } else if (solrDocument != null) {
      throw new UnsupportedOperationException(
          "Solr relationships not supported yet: " + documentClass.getName()
              + "." + field.getName());
    } else {
      documentSource = createPropertySource(documentClass, field, readable,
          writable, newPath, multivaluedNow, targetType);
    }
    if (newMultivalued) {
      documentSource = createNewCollectionSource(documentClass, field,
          fieldType, componentType, readable, writable,
          (MultiValuableDocumentSource) documentSource);
    }
    return documentSource;
  }

  private boolean isWritable(Field field, boolean isId) {
    boolean writable;
    SolrWritable solrWritable = field.getAnnotation(SolrWritable.class);
    if (solrWritable == null) {
      writable = true;
    } else {
      writable = solrWritable.value();
    }
    if (isId && !writable) {
      throw new BindingException("Id property must  be writable: "
          + field.getDeclaringClass().getName() + "." + field.getName());
    }
    return writable;
  }

  private boolean isReadable(Field field, boolean isId) {
    boolean readable;
    SolrReadable solrReadable = field.getAnnotation(SolrReadable.class);
    if (solrReadable == null) {
      readable = true;
    } else {
      readable = solrReadable.value();
    }
    if (isId && !readable) {
      throw new BindingException("Id property must be readable: "
          + field.getDeclaringClass().getName() + "." + field.getName());
    }
    return readable;
  }

  private PropertySource createPropertySource(Class<?> documentClass,
      Field field, boolean readable, boolean writable, String path,
      boolean multivalued, Class<?> targetType) {
    ValueConverterFactory converterFactory;
    if (Enum.class.isAssignableFrom(targetType)) {
      converterFactory = new EnumValueConverterFactory();
    } else {
      converterFactory = binder.getValueConverterFactory(targetType);
    }
    ValueConverter converter = converterFactory.create(targetType,
        documentClass, field, path, binder.getFieldPathSeprator());

    if (multivalued) {
      if (converter instanceof MultiSlotValueConverter) {
        return new MultiSlotMultiValuedPropertySource(targetType, field,
            readable, writable, binder.isNullTrickEnabled(),
            (MultiSlotValueConverter) converter);
      } else {
        return new SingleSlotMultiValuedPropertySource(targetType, field, path,
            readable, writable, binder.isNullTrickEnabled(),
            (SingleSlotValueConverter) converter);
      }
    } else {
      if (converter instanceof MultiSlotValueConverter) {
        return new MultiSlotNotMultiValuedPropertySource(targetType, field,
            readable, writable, (MultiSlotValueConverter) converter);
      } else {
        return new SingleSlotNotMultiValuedPropertySource(targetType, field,
            readable, writable, (SingleSlotValueConverter) converter);
      }
    }
  }

  private EmbeddedSource createEmbeddedSource(Class<?> documentClass,
      Field field, boolean readable, boolean writable, String path,
      boolean multivalued, Class<?> targetType) {
    ClassSource embeddedClasSource;
    FieldSource[] fieldSources = findFieldSources(targetType, null, path,
        multivalued);

    BeanInstantiator instantiator = binder.createBeanInstantiator(targetType);
    if (multivalued) {
      embeddedClasSource = new MultiValuedEmbeddableClassSource(targetType,
          instantiator, fieldSources, path);
    } else {
      embeddedClasSource = new NotMultiValuedEmbeddableClassSource(targetType,
          instantiator, fieldSources);
    }
    return new EmbeddedSource(targetType, field, readable, writable,
        embeddedClasSource);
  }

  private FieldSource createNewCollectionSource(Class<?> documentClass,
      Field field, Class<?> collectionType, Class<?> componentType,
      boolean readable, boolean writable,
      MultiValuableDocumentSource documentSource) {
    if (collectionType.isArray()) {
      return new ArraySource(documentClass, field, componentType,
          documentSource, readable, writable);
    } else {
      CollectionCreatorFactory collectionCreatorFactory = binder
          .getCollectionCreatorFactory(collectionType);
      if (collectionCreatorFactory == null) {
        throw new BindingException("Solr field " + documentClass.getName()
            + "." + field.getName()
            + " refers to non supported collection type "
            + collectionType.getName());
      }
      CollectionCreator collectionCreator = collectionCreatorFactory.create(
          collectionType, componentType, documentClass, field);
      return new CollectionSource(documentClass, field, collectionType,
          componentType, collectionCreator, documentSource, readable, writable);
    }
  }

  protected Class<?> getFieldType(Class<?> documentClass, Field field) {
    Class<?> type = field.getType();
    SolrTargetCollection solrTargetCollection = field
        .getAnnotation(SolrTargetCollection.class);
    if (type.isArray()) {
      if (solrTargetCollection != null) {
        throw new BindingException("Array declaration of "
            + documentClass.getName() + ", cannot use @SolrTargetCollection: "
            + field.getDeclaringClass().getName() + "." + field.getName());
      }
      return type;
    } else if (Collection.class.isAssignableFrom(type)) {
      if (solrTargetCollection == null) {
        return type;
      }
      if (!type.isAssignableFrom(solrTargetCollection.value())) {
        throw new BindingException("Collection field of"
            + documentClass.getName() + " "
            + field.getDeclaringClass().getName() + "." + field.getName()
            + " declares to be  " + type.getName()
            + " but @SolrTargetCollection is "
            + solrTargetCollection.value().getName()
            + ", which is not a subclass or implements the former.");
      }
      return solrTargetCollection.value();
    } else {
      SolrTarget solrTarget = field.getAnnotation(SolrTarget.class);
      if (solrTarget == null) {
        return type;
      }
      if (!type.isAssignableFrom(solrTarget.value())) {
        throw new BindingException("Field of" + documentClass.getName() + " "
            + field.getDeclaringClass().getName() + "." + field.getName()
            + " declares to be  " + type.getName() + " but @SolrTarget is "
            + solrTarget.value().getName()
            + ", which is not a subclass or implements the former.");
      }
      return solrTarget.value();
    }

  }

  protected Class<?> getFieldComponentType(Class<?> documentClass, Field field) {
    Class<?> type = field.getType();
    SolrTarget solrTarget = field.getAnnotation(SolrTarget.class);
    Class<?> componentType = null;
    if (type.isArray()) {
      componentType = type.getComponentType();
    } else if (Collection.class.isAssignableFrom(type)) {
      componentType = extractCollectionComponent(field);
      if (componentType == null) {
        if (solrTarget == null) {
          throw new BindingException("Unsupported collection declaration of "
              + documentClass.getName() + ", use @SolrTarget to disambiguate: "
              + field.getDeclaringClass().getName() + "." + field.getName());
        } else {
          componentType = Object.class;
        }
      }
    } else {
      return null;
    }
    if (solrTarget == null) {
      return componentType;
    }
    if (!componentType.isAssignableFrom(solrTarget.value())) {
      throw new BindingException("Collection field of"
          + documentClass.getName() + " " + field.getDeclaringClass().getName()
          + "." + field.getName() + " declares to contain "
          + componentType.getName() + " but @SolrTarget is "
          + solrTarget.value().getName()
          + ", which is not a subclass or implements the former.");
    }
    return solrTarget.value();
  }

  private Class<?> extractCollectionComponent(Field field) {
    Type genericType = field.getGenericType();
    if (genericType instanceof Class) {
      return Object.class;
    } else if (genericType instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) genericType;
      Type[] typeArguments = parameterizedType.getActualTypeArguments();
      if (typeArguments.length == 1) {
        Type typeArgument = typeArguments[0];
        if (typeArgument instanceof Class) {
          return (Class<?>) typeArgument;
        }
      }
    }
    return null;
  }

}
