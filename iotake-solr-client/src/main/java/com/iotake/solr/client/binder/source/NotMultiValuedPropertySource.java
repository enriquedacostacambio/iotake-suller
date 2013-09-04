package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public abstract class NotMultiValuedPropertySource extends PropertySource {

  public NotMultiValuedPropertySource(Class<?> documentClass, Field field,
      boolean readable, boolean writable) {
    super(documentClass, field, readable, writable);
  }

  protected void set(SolrInputDocument document, Slot slot,
      Object documentValue) {
    document.setField(slot.getPath(), documentValue);
  }

  protected Object getSingle(SolrDocument document, Slot slot) {
    Object documentValue = document.getFieldValue(slot.getPath());
    if (documentValue instanceof Collection) {
      throw new BindingException("Document value at " + slot.getPath()
          + " is a collection, but " + getDocumentClass().getName() + "."
          + getField().getName() + " is expecting a single value of type "
          + slot.getType() + " at this location.");
    }
    return documentValue;
  }

}