package com.iotake.solr.client.binder.source;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrInputDocument;

public abstract class ClassSource extends AbstractDocumentSource {

  private final FieldSource[] fieldSources;
  private final Constructor<?> constructor;

  public ClassSource(Class<?> documentClass, FieldSource[] fieldSources) {
    super(documentClass);
    this.fieldSources = fieldSources;
    this.constructor = findConstructor();
  }

  private Constructor<?> findConstructor() {
    try {
      Constructor<?> constructor = getDocumentClass().getDeclaredConstructor();
      constructor.setAccessible(true);
      return constructor;
    } catch (NoSuchMethodException e) {
      throw new BindingException("Error creating document "
          + getDocumentClass().getName() + ": " + e, e);
    }
  }

  protected FieldSource[] getFieldSources() {
    return fieldSources;
  }

  public Object newInstance() {
    try {
      return constructor.newInstance();
    } catch (InstantiationException e) {
      throw new BindingException("Error creating document "
          + getDocumentClass().getName() + ": " + e, e);
    } catch (IllegalAccessException e) {
      throw new BindingException("Error creating document "
          + getDocumentClass().getName() + ": " + e, e);
    } catch (InvocationTargetException e) {
      throw new BindingException("Error creating document "
          + getDocumentClass().getName() + ": " + e, e);
    }
  }

  public DocumentSource[] getInnerSources() {
    return fieldSources.clone();
  }

  public void doTransfer(Object bean, SolrInputDocument document) {
    for (FieldSource fieldSource : getFieldSources()) {
      Object fieldValue = fieldSource.getFieldValue(bean);
      fieldSource.transfer(fieldValue, document);
    }
  }

}