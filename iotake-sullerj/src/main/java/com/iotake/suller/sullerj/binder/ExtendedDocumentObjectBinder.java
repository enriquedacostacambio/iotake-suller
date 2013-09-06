package com.iotake.suller.sullerj.binder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;
import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiatorFactory;
import com.iotake.suller.sullerj.binder.postprocessor.BeanPostProcessor;
import com.iotake.suller.sullerj.binder.postprocessor.DocumentPostProcessor;
import com.iotake.suller.sullerj.binder.source.DocumentSourceBuilder;
import com.iotake.suller.sullerj.binder.source.RootClassSource;
import com.iotake.suller.sullerj.binder.value.ValueConverterFactory;
import com.iotake.suller.sullerj.session.SessionFactory;
import com.iotake.suller.sullerj.session.WorkingSession;
import com.iotake.suller.sullerj.session.WorkingSessionFactory;

public class ExtendedDocumentObjectBinder extends DocumentObjectBinder {

  private final WorkingSessionFactory sessionFactory;
  private final DocumentSourceBuilder sourceBuilder;
  private final ClassLoader classLoader;
  private final String fieldPathSeprator;
  private final String globalIdFieldName;
  private final String classFieldName;
  private final String classesFieldName;
  private final Map<Class<?>, CollectionCreatorFactory> collectionCreatorFactories;
  private final Map<Class<?>, ValueConverterFactory> valueConverterFactories;
  private final ValueConverterFactory fallbackValueConverterFactory;
  private final BeanInstantiatorFactory beanInstantiatorFactory;
  private final List<BeanPostProcessor> beanPostProcessors;
  private final List<DocumentPostProcessor> documentPostProcessors;
  private final boolean nullTrickEnabled;

  public ExtendedDocumentObjectBinder() {
    this(
        ExtendedDocumentObjectBinderBuilder.DEFAULT_SESSION_FACTORY,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_FIELD_PATH_SEPARATOR,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_GLOBAL_ID_FIELD_NAME,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_CLASS_FIELD_NAME,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_CLASSES_FIELD_NAME,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_CLASS_LOADER,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_COLLECTION_CREATOR_FACTORIES,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_VALUE_CONVERTER_FACTORIES,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_FALLBACK_CONVERTER_FACTORY,
        ExtendedDocumentObjectBinderBuilder.DEFAULT_BEAN_INSTANTIATOR_FACTORY,
        Collections.<BeanPostProcessor> emptyList(), Collections
            .<DocumentPostProcessor> emptyList(),
        ExtendedDocumentObjectBinderBuilder.DEFAULT_NULL_TRICK_ENABLED);
  }

  public ExtendedDocumentObjectBinder(WorkingSessionFactory sessionFactory,
      String fieldPathSeprator, String globalIdFieldName,
      String classFieldName, String classesFieldName, ClassLoader classLoader,
      Map<Class<?>, CollectionCreatorFactory> collectionCreatorFactories,
      Map<Class<?>, ValueConverterFactory> valueConverterFactories,
      ValueConverterFactory fallbackValueConverterFactory,
      BeanInstantiatorFactory beanInstantiatorFactory,
      List<BeanPostProcessor> beanPostProcessors,
      List<DocumentPostProcessor> documentPostProcessors,
      boolean nullTrickEnabled) {
    this.sessionFactory = sessionFactory;
    this.fieldPathSeprator = fieldPathSeprator;
    this.globalIdFieldName = globalIdFieldName;
    this.classFieldName = classFieldName;
    this.classesFieldName = classesFieldName;
    this.classLoader = classLoader;
    this.collectionCreatorFactories = new HashMap<Class<?>, CollectionCreatorFactory>(
        collectionCreatorFactories);
    this.valueConverterFactories = new HashMap<Class<?>, ValueConverterFactory>(
        valueConverterFactories);
    this.fallbackValueConverterFactory = fallbackValueConverterFactory;
    this.beanInstantiatorFactory = beanInstantiatorFactory;
    this.beanPostProcessors = new ArrayList<BeanPostProcessor>(
        beanPostProcessors);
    this.documentPostProcessors = new ArrayList<DocumentPostProcessor>(
        documentPostProcessors);
    this.nullTrickEnabled = nullTrickEnabled;
    this.sourceBuilder = new DocumentSourceBuilder(this);
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public String getFieldPathSeprator() {
    return fieldPathSeprator;
  }

  public String getGlobalIdFieldName() {
    return globalIdFieldName;
  }

  public String getClassFieldName() {
    return classFieldName;
  }

  public String getClassesFieldName() {
    return classesFieldName;
  }

  private Map<Class<?>, RootClassSource> rootClassSources = new HashMap<Class<?>, RootClassSource>();

  public RootClassSource getRootClassSource(Class<?> beanClass) {
    synchronized (rootClassSources) {
      RootClassSource rootClassSource = rootClassSources.get(beanClass);
      if (rootClassSource == null) {
        rootClassSource = sourceBuilder.create(beanClass);
        rootClassSources.put(beanClass, rootClassSource);
      }
      return rootClassSource;
    }
  }

  public ValueConverterFactory getValueConverterFactory(Class<?> targetType) {
    ValueConverterFactory factory = valueConverterFactories.get(targetType);
    if (factory == null) {
      factory = fallbackValueConverterFactory;
    }
    return factory;
  }

  public CollectionCreatorFactory getCollectionCreatorFactory(
      Class<?> collectionType) {
    return collectionCreatorFactories.get(collectionType);
  }

  public BeanInstantiator createBeanInstantiator(Class<?> beanClass) {
    try {
      return beanInstantiatorFactory.create(beanClass);
    } catch (Exception e) {
      throw new BindingException("Error creating bean instantiator for class "
          + beanClass.getName(), e);
    }
  }

  private WorkingSession getCurrentSession() {
    return sessionFactory.current();
  }

  public <T> T getBean(SolrDocument document) {
    return getBean(null, document);
  }

  @Override
  public <T> T getBean(Class<T> suggestedBeanClass, SolrDocument document) {
    Object className = document.get(classFieldName);
    if (className == null || !(className instanceof String)) {
      throw new BindingException("No valid class field value: " + className);
    }
    @SuppressWarnings("unchecked")
    Class<T> beanClass = (Class<T>) getBeanClass((String) className);

    RootClassSource rootClassSource = getRootClassSource(beanClass);
    Object bean;
    try {
      WorkingSession session = getCurrentSession();
      if (session == null) {
        bean = extract(rootClassSource, document);
      } else {
        Object id = rootClassSource.getId(document);
        bean = session.lookup(beanClass, id);
        if (bean == null) {
          bean = extract(rootClassSource, document);
          session.register(id, beanClass.cast(bean));
        }
      }
      return beanClass.cast(bean);
    } catch (Exception e) {
      throw new BindingException("Error constructing bean of class: "
          + className + " from document " + document, e);
    }
  }

  private Object extract(RootClassSource rootClassSource, SolrDocument document)
      throws Exception {
    Object bean = rootClassSource.extract(document);
    Object postBean = postProcess(document, bean);
    return postBean;
  }

  public <T> List<T> getBeans(SolrDocumentList documents) {
    return getBeans(null, documents);
  }

  @Override
  public <T> List<T> getBeans(Class<T> suggestedBeanClass,
      SolrDocumentList documents) {
    List<T> beans = new ArrayList<T>(documents.size());
    for (SolrDocument document : documents) {
      T bean = getBean(suggestedBeanClass, document);
      beans.add(bean);
    }
    return beans;
  }

  private Class<?> getBeanClass(String className) {
    try {
      return classLoader == null ? Class.forName(className) : classLoader
          .loadClass((String) className);
    } catch (ClassNotFoundException e) {
      throw new BindingException("Could't load bean class: " + className, e);
    }
  }

  @Override
  public SolrInputDocument toSolrInputDocument(Object bean) {
    Class<?> beanClass = bean.getClass();
    RootClassSource rootClassSource = getRootClassSource(beanClass);
    try {
      SolrInputDocument document = transfer(bean, rootClassSource);

      Object id = rootClassSource.getId(bean);
      WorkingSession session = getCurrentSession();
      if (session != null) {
        session.register(id, bean);
      }
      return document;
    } catch (Exception e) {
      throw new BindingException(
          "Error constructing document for bean of class "
              + beanClass.getName() + ":" + bean, e);
    }
  }

  private SolrInputDocument transfer(Object bean,
      RootClassSource rootClassSource) throws Exception {
    SolrInputDocument document = rootClassSource.transfer(bean);
    SolrInputDocument postDocument = postProcess(bean, document);
    return postDocument;
  }

  private SolrInputDocument postProcess(Object bean, SolrInputDocument document)
      throws Exception {
    for (DocumentPostProcessor documentPostProcessor : documentPostProcessors) {
      document = documentPostProcessor.postProcess(bean, document);
    }
    return document;
  }

  private Object postProcess(SolrDocument document, Object bean)
      throws Exception {
    for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
      bean = beanPostProcessor.postProcess(document, bean);
    }
    return bean;
  }

  public boolean isNullTrickEnabled() {
    return nullTrickEnabled;
  }
}
