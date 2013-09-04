package com.iotake.solr.client.binder.source;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;

public class RootClassSource extends NotMultiValuedClassSource {

  private final FieldSource idFieldSource;

  private final Set<String> classes;

  private final String classFieldName;

  private final String classesFieldName;

  public RootClassSource(Class<?> documentClass, BeanInstantiator instantiator,
      FieldSource idFieldSource, FieldSource[] fieldSources,
      String classFieldName, String classesFieldName) {
    super(documentClass, instantiator, fieldSources);
    this.idFieldSource = idFieldSource;
    Set<String> classes = new LinkedHashSet<String>();
    collectClasses(documentClass, classes);
    this.classes = classes;
    this.classFieldName = classFieldName;
    this.classesFieldName = classesFieldName;

  }

  private void collectClasses(Class<?> currentClass, Set<String> classes) {
    String className = currentClass.getName();
    classes.add(className);

    Class<?> superclass = currentClass.getSuperclass();
    if (superclass != null) {
      collectClasses(superclass, classes);
    }
    for (Class<?> anInterface : currentClass.getInterfaces()) {
      collectClasses(anInterface, classes);
    }
  }

  public SourceType getType() {
    return SourceType.ROOT_CLASS;
  }

  @Override
  public void doTransfer(Object bean, SolrInputDocument document) {
    super.doTransfer(bean, document);
    Object id = idFieldSource.getFieldValue(bean);
    idFieldSource.transfer(id, document);
    document.setField(classFieldName, getDocumentClass().getName());
    document.setField(classesFieldName, classes);
  }

  public SolrInputDocument transfer(Object bean) {
    SolrInputDocument document = new SolrInputDocument();
    transfer(bean, document);
    return document;
  }

  public Object getId(SolrDocument document) {
    Object id = idFieldSource.extract(document);
    return id;
  }

  public Object getId(Object bean) {
    Object id = idFieldSource.getFieldValue(bean);
    return id;
  }

  @Override
  public Object doExtract(SolrDocument document) {
    Object bean = super.doExtract(document);
    Object id = idFieldSource.extract(document);
    idFieldSource.setFieldValue(bean, id);
    return bean;
  }

  @Override
  public DocumentSource[] getInnerSources() {
    DocumentSource[] parentInnerSources = super.getInnerSources();
    DocumentSource[] innerSources = new DocumentSource[parentInnerSources.length + 1];
    System.arraycopy(parentInnerSources, 0, innerSources, 0,
        parentInnerSources.length);
    innerSources[parentInnerSources.length + 0] = idFieldSource;
    return innerSources;
  }
}