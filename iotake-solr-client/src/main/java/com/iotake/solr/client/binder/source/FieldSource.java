package com.iotake.solr.client.binder.source;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public abstract class FieldSource extends AbstractDocumentSource {

  private final Field field;

  private final boolean readable;

  private final boolean writable;

  public FieldSource(Class<?> documentClass, Field field, boolean readable,
      boolean writable) {
    super(documentClass);
    checkNotNull(field, "Field cannot be null.");
    if (!readable && field.getType().isPrimitive())
      throw new BindingException(
          "Field type cannot be primitive if it is not readable "
              + field.getDeclaringClass().getName() + "." + field.getName());
    this.field = field;
    field.setAccessible(true);
    this.readable = readable;
    this.writable = writable;
  }

  public Field getField() {
    return field;
  }

  protected Object doExtract(SolrDocument document) {
    if (!readable) {
      return null;
    }
    return doFieldExtract(document);
  }

  protected void doTransfer(Object value, SolrInputDocument document) {
    if (!writable) {
      return;
    }
    doFieldTransfer(value, document);
  }

  protected abstract Object doFieldExtract(SolrDocument document);

  protected abstract void doFieldTransfer(Object value,
      SolrInputDocument document);

  protected Object getFieldValue(Object object) {
    try {
      if (object == null) {
        return null;
      }
      return field.get(object);
    } catch (IllegalAccessException e) {
      throw new BindingException("Error extracting value from "
          + field.getDeclaringClass().getName() + "." + field.getName() + ": "
          + e, e);
    }
  }

  protected void setFieldValue(Object object, Object fieldValue) {
    try {
      field.set(object, fieldValue);
    } catch (IllegalAccessException e) {
      throw new BindingException("Error extracting value from "
          + field.getDeclaringClass().getName() + "." + field.getName() + ": "
          + e, e);
    }
  }

}