package com.iotake.solr.client.binder.collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.Collection;

public abstract class AbstractCollectionCreatorFactory implements
    CollectionCreatorFactory {

  private Class<?> collectionImplementationType;

  public AbstractCollectionCreatorFactory(Class<?> collectionImplementationType) {
    checkNotNull(collectionImplementationType,
        "Collection implementation type cannot be null.");
    checkArgument(
        Collection.class.isAssignableFrom(collectionImplementationType),
        "Collection implementation type "
            + collectionImplementationType.getName() + " must implement "
            + Collection.class.getName() + ".");
    this.collectionImplementationType = collectionImplementationType;
  }

  public CollectionCreator create(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field) {
    checkNotNull(collectionType, "Collection type cannot be null.");
    checkArgument(
        collectionType.isAssignableFrom(collectionImplementationType),
        "Collection type " + collectionType.getName() + " must be "
            + collectionImplementationType.getName()
            + " or a superclass extended or interface implemented by "
            + collectionImplementationType.getName() + ".");
    checkNotNull(componentType, "Component type cannot be null.");
    checkNotNull(documentClass, "Document class cannot be null.");
    checkNotNull(field, "Field cannot be null.");
    return doCreate(collectionType, componentType, documentClass, field);
  }

  public abstract CollectionCreator doCreate(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field);

}
