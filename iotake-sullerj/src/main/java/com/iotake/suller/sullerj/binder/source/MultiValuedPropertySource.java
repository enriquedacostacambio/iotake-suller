package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public abstract class MultiValuedPropertySource extends PropertySource
    implements MultiValuableDocumentSource {

  public static final Object NULL_TRICK = new Object() {
    public String toString() {
      return null;
    }
  };

  private final boolean nullTrickEnabled;

  public MultiValuedPropertySource(Class<?> documentClass, Field field,
      boolean readable, boolean writable, boolean nullTrickEnabled) {
    super(documentClass, field, readable, writable);
    this.nullTrickEnabled = nullTrickEnabled;
  }

  public void setEmpty(SolrInputDocument document, Slot slot) {
    String slotPath = slot.getPath();
    document.setField(slotPath, Collections.EMPTY_LIST);
  }

  protected void add(SolrInputDocument document, Slot slot, Object documentValue) {
    String slotPath = slot.getPath();
    Collection<Object> documentValues = document.getFieldValues(slotPath);
    if (documentValues == null) {
      documentValues = new LinkedList<Object>();
      document.setField(slotPath, documentValues);
    }
    Object storedValue = documentValue;
    if (storedValue == null && nullTrickEnabled) {
      storedValue = NULL_TRICK;
    }
    documentValues.add(storedValue);
  }

  protected Collection<?> getCollection(SolrDocument document, Slot slot) {
    Object documentValues = document.getFieldValue(slot.getPath());
    if (documentValues != null & !(documentValues instanceof Collection)) {
      throw new BindingException("Document value at " + slot.getPath()
          + " is not a single value, but " + getDocumentClass().getName() + "."
          + getField().getName() + " is expecting a collection of type "
          + slot.getType() + " at this location: "
          + documentValues.getClass().getName());
    }
    return (Collection<?>) documentValues;
  }

}