package com.iotake.solr.client.binder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iotake.solr.client.binder.collection.ArrayListCreatorFactory;
import com.iotake.solr.client.binder.collection.CollectionCreatorFactory;
import com.iotake.solr.client.binder.collection.HashSetCreatorFactory;
import com.iotake.solr.client.binder.collection.LinkedHashSetCreatorFactory;
import com.iotake.solr.client.binder.collection.LinkedListCreatorFactory;
import com.iotake.solr.client.binder.postprocessor.BeanPostProcessor;
import com.iotake.solr.client.binder.postprocessor.DocumentPostProcessor;
import com.iotake.solr.client.binder.session.SessionContext;
import com.iotake.solr.client.binder.session.impl.NullSessionContext;
import com.iotake.solr.client.binder.value.BigDecimalValueConverterFactory;
import com.iotake.solr.client.binder.value.BigIntegerValueConverterFactory;
import com.iotake.solr.client.binder.value.NativeValueConverterFactory;
import com.iotake.solr.client.binder.value.NumberValueConverterFactory;
import com.iotake.solr.client.binder.value.StringifiableValueConverterFactory;
import com.iotake.solr.client.binder.value.ValueConverterFactory;

public class ExtendedDocumentObjectBinderBuilder {

  public static final SessionContext DEFAULT_SESSION_CONTEXT = NullSessionContext.INSTANCE;
  public static final String DEFAULT_FIELD_PATH_SEPARATOR = "__";
  public static final String DEFAULT_GLOBAL_ID_FIELD_NAME = "id";
  public static final String DEFAULT_CLASS_FIELD_NAME = "class";
  public static final String DEFAULT_CLASSES_FIELD_NAME = "classes";
  public static final ClassLoader DEFAULT_CLASS_LOADER = null;
  public static final boolean DEFAULT_APPEND_DEFAULT_VALUE_CONVERTER_FACTORIES = true;
  public static final boolean DEFAULT_APPEND_COLLECTION_CREATOR_FACTORIES = true;
  public static final boolean DEFAULT_NULL_TRICK_ENABLED = true;

  public static final Map<Class<?>, ValueConverterFactory> DEFAULT_VALUE_CONVERTER_FACTORIES;
  static {
    Map<Class<?>, ValueConverterFactory> factories = new HashMap<Class<?>, ValueConverterFactory>();

    ValueConverterFactory nativeValueConverterFactory = new NativeValueConverterFactory();
    factories.put(String.class, nativeValueConverterFactory);
    factories.put(Date.class, nativeValueConverterFactory);
    factories.put(boolean.class, nativeValueConverterFactory);
    factories.put(char.class, nativeValueConverterFactory);
    factories.put(byte.class, nativeValueConverterFactory);
    factories.put(short.class, nativeValueConverterFactory);
    factories.put(int.class, nativeValueConverterFactory);
    factories.put(long.class, nativeValueConverterFactory);
    factories.put(float.class, nativeValueConverterFactory);
    factories.put(double.class, nativeValueConverterFactory);
    factories.put(Boolean.class, nativeValueConverterFactory);
    factories.put(Character.class, nativeValueConverterFactory);
    factories.put(Byte.class, nativeValueConverterFactory);
    factories.put(Short.class, nativeValueConverterFactory);
    factories.put(Integer.class, nativeValueConverterFactory);
    factories.put(Long.class, nativeValueConverterFactory);
    factories.put(Float.class, nativeValueConverterFactory);
    factories.put(Double.class, nativeValueConverterFactory);

    factories.put(Number.class, new NumberValueConverterFactory());
    factories.put(BigDecimal.class, new BigDecimalValueConverterFactory());
    factories.put(BigInteger.class, new BigIntegerValueConverterFactory());

    DEFAULT_VALUE_CONVERTER_FACTORIES = Collections.unmodifiableMap(factories);
  }

  public static final ValueConverterFactory DEFAULT_FALLBACK_CONVERTER_FACTORY = new StringifiableValueConverterFactory();

  public static final Map<Class<?>, CollectionCreatorFactory> DEFAULT_COLLECTION_CREATOR_FACTORIES;
  static {
    Map<Class<?>, CollectionCreatorFactory> factories = new HashMap<Class<?>, CollectionCreatorFactory>();
    CollectionCreatorFactory arrayListFactory = new ArrayListCreatorFactory();
    factories.put(Iterable.class, arrayListFactory);
    factories.put(Collection.class, arrayListFactory);
    factories.put(List.class, arrayListFactory);
    factories.put(ArrayList.class, arrayListFactory);
    factories.put(LinkedList.class, new LinkedListCreatorFactory());
    CollectionCreatorFactory hashSetFactory = new HashSetCreatorFactory();
    factories.put(Set.class, hashSetFactory);
    factories.put(HashSet.class, hashSetFactory);
    factories.put(LinkedHashSet.class, new LinkedHashSetCreatorFactory());

    DEFAULT_COLLECTION_CREATOR_FACTORIES = Collections
        .unmodifiableMap(factories);
  }

  private volatile SessionContext sessionContext = DEFAULT_SESSION_CONTEXT;
  private volatile ClassLoader classLoader = DEFAULT_CLASS_LOADER;
  private volatile String fieldPathSeprator = DEFAULT_FIELD_PATH_SEPARATOR;
  private volatile String globalIdFieldName = DEFAULT_GLOBAL_ID_FIELD_NAME;
  private volatile String classFieldName = DEFAULT_CLASS_FIELD_NAME;
  private volatile String classesFieldName = DEFAULT_CLASSES_FIELD_NAME;
  private volatile boolean appendDefaultCollectionCreatorFactories = DEFAULT_APPEND_DEFAULT_VALUE_CONVERTER_FACTORIES;
  private volatile boolean appendDefaultValueConverterFactories = DEFAULT_APPEND_COLLECTION_CREATOR_FACTORIES;
  private volatile Map<Class<?>, CollectionCreatorFactory> collectionCreatorFactories = new HashMap<Class<?>, CollectionCreatorFactory>();
  private volatile Map<Class<?>, ValueConverterFactory> valueConverterFactories = new HashMap<Class<?>, ValueConverterFactory>();
  private volatile ValueConverterFactory fallbackValueConverterFactory = DEFAULT_FALLBACK_CONVERTER_FACTORY;
  private volatile List<BeanPostProcessor> beanPostProcessors = new LinkedList<BeanPostProcessor>();
  private volatile List<DocumentPostProcessor> documentPostProcessors = new LinkedList<DocumentPostProcessor>();
  private volatile boolean nullTrickEnabled = DEFAULT_NULL_TRICK_ENABLED;

  public ExtendedDocumentObjectBinderBuilder() {
  }

  public SessionContext getSessionContext() {
    return sessionContext;
  }

  public ExtendedDocumentObjectBinderBuilder setSessionContext(
      SessionContext sessionContext) {
    this.sessionContext = sessionContext;
    return this;
  }

  public ClassLoader getClassLoader() {
    return classLoader;
  }

  public ExtendedDocumentObjectBinderBuilder setClassLoader(
      ClassLoader classLoader) {
    this.classLoader = classLoader;
    return this;
  }

  public String getFieldPathSeprator() {
    return fieldPathSeprator;
  }

  public ExtendedDocumentObjectBinderBuilder setFieldPathSeprator(
      String fieldPathSeprator) {
    this.fieldPathSeprator = fieldPathSeprator;
    return this;
  }

  public String getGlobalIdFieldName() {
    return globalIdFieldName;
  }

  public ExtendedDocumentObjectBinderBuilder setGlobalIdFieldName(
      String globalIdFieldName) {
    this.globalIdFieldName = globalIdFieldName;
    return this;
  }

  public String getClassFieldName() {
    return classFieldName;
  }

  public ExtendedDocumentObjectBinderBuilder setClassFieldName(
      String classFieldName) {
    this.classFieldName = classFieldName;
    return this;
  }

  public String getClassesFieldName() {
    return classesFieldName;
  }

  public ExtendedDocumentObjectBinderBuilder setClassesFieldName(
      String classesFieldName) {
    this.classesFieldName = classesFieldName;
    return this;
  }

  public boolean isAppendDefaultCollectionCreatorFactories() {
    return appendDefaultCollectionCreatorFactories;
  }

  public ExtendedDocumentObjectBinderBuilder setAppendDefaultCollectionCreatorFactories(
      boolean useCollectionCreatorFactories) {
    this.appendDefaultCollectionCreatorFactories = useCollectionCreatorFactories;
    return this;
  }

  public boolean isAppendDefaultValueConverterFactories() {
    return appendDefaultValueConverterFactories;
  }

  public ExtendedDocumentObjectBinderBuilder setAppendDefaultValueConverterFactories(
      boolean useValueConverterFactories) {
    this.appendDefaultValueConverterFactories = useValueConverterFactories;
    return this;
  }

  public Map<Class<?>, CollectionCreatorFactory> getCollectionCreatorFactories() {
    return collectionCreatorFactories;
  }

  public ExtendedDocumentObjectBinderBuilder setCollectionCreatorFactories(
      Map<Class<?>, CollectionCreatorFactory> collectionCreatorFactories) {
    this.collectionCreatorFactories = collectionCreatorFactories;
    return this;
  }

  public ExtendedDocumentObjectBinderBuilder addCollectionCreatorFactory(
      Class<?> collectionClass,
      CollectionCreatorFactory collectionCreatorFactory) {
    this.collectionCreatorFactories.put(collectionClass,
        collectionCreatorFactory);
    return this;
  }

  public Map<Class<?>, ValueConverterFactory> getValueConverterFactories() {
    return valueConverterFactories;
  }

  public ExtendedDocumentObjectBinderBuilder setValueConverterFactories(
      Map<Class<?>, ValueConverterFactory> valueConverterFactories) {
    this.valueConverterFactories = valueConverterFactories;
    return this;
  }

  public ExtendedDocumentObjectBinderBuilder addValueConverterFactory(
      Class<?> valueClass, ValueConverterFactory valueConverterFactory) {
    this.valueConverterFactories.put(valueClass, valueConverterFactory);
    return this;
  }

  public ValueConverterFactory getFallbackValueConverterFactory() {
    return fallbackValueConverterFactory;
  }

  public ExtendedDocumentObjectBinderBuilder setFallbackValueConverterFactory(
      ValueConverterFactory fallbackValueConverterFactory) {
    this.fallbackValueConverterFactory = fallbackValueConverterFactory;
    return this;
  }

  public List<BeanPostProcessor> getBeanPostProcessors() {
    return beanPostProcessors;
  }

  public ExtendedDocumentObjectBinderBuilder setBeanPostProcessors(
      List<BeanPostProcessor> beanPostProcessors) {
    this.beanPostProcessors = beanPostProcessors;
    return this;
  }

  public ExtendedDocumentObjectBinderBuilder addBeanPostProcessors(
      BeanPostProcessor beanPostProcessor) {
    this.beanPostProcessors.add(beanPostProcessor);
    return this;
  }

  public List<DocumentPostProcessor> getDocumentPostProcessors() {
    return documentPostProcessors;
  }

  public ExtendedDocumentObjectBinderBuilder setDocumentPostProcessors(
      List<DocumentPostProcessor> documentPostProcessors) {
    this.documentPostProcessors = documentPostProcessors;
    return this;
  }

  public ExtendedDocumentObjectBinderBuilder addDocumentPostProcessors(
      DocumentPostProcessor documentPostProcessor) {
    this.documentPostProcessors.add(documentPostProcessor);
    return this;
  }

  public boolean isNullTrickEnabled() {
    return nullTrickEnabled;
  }

  public ExtendedDocumentObjectBinderBuilder setNullTrickEnabled(
      boolean nullTrickEnabled) {
    this.nullTrickEnabled = nullTrickEnabled;
    return this;
  }

  public ExtendedDocumentObjectBinder build() {
    Map<Class<?>, CollectionCreatorFactory> collectionCreatorFactories = new HashMap<Class<?>, CollectionCreatorFactory>();
    if (appendDefaultCollectionCreatorFactories) {
      collectionCreatorFactories.putAll(DEFAULT_COLLECTION_CREATOR_FACTORIES);
    }
    collectionCreatorFactories.putAll(this.collectionCreatorFactories);
    Map<Class<?>, ValueConverterFactory> valueConverterFactories = new HashMap<Class<?>, ValueConverterFactory>();
    if (appendDefaultValueConverterFactories) {
      valueConverterFactories.putAll(DEFAULT_VALUE_CONVERTER_FACTORIES);
    }
    valueConverterFactories.putAll(this.valueConverterFactories);
    return new ExtendedDocumentObjectBinder(sessionContext, fieldPathSeprator,
        globalIdFieldName, classFieldName, classesFieldName, classLoader,
        collectionCreatorFactories, valueConverterFactories,
        fallbackValueConverterFactory, beanPostProcessors,
        documentPostProcessors, nullTrickEnabled);
  }
}
