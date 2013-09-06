package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.suller.sullerj.binder.value.MultiSlotValueConverter;

public class MultiSlotNotMultiValuedPropertySource extends
    NotMultiValuedPropertySource {

  private final MultiSlotValueConverter converter;

  public MultiSlotNotMultiValuedPropertySource(Class<?> documentClass,
      Field field, boolean readable, boolean writable,
      MultiSlotValueConverter converter) {
    super(documentClass, field, readable, writable);
    this.converter = converter;
  }

  @Override
  public Slot[] getSlots() {
    return converter.getSlots().clone();
  }
  
  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    Slot[] slots = converter.getSlots();
    Object[] documentValueSlots = converter.toDocumentValues(value);
    if (documentValueSlots.length != slots.length) {
      throw new BindingException(
          "Invalid number of documentValues returned from converter for "
              + getDocumentClass().getName() + "." + getField().getName());
    }
    for (int i = 0; i < documentValueSlots.length; i++) {
      Object documentValueSlot = documentValueSlots[i];
      Slot slot = slots[i];
      set(document, slot, documentValueSlot);
    }
  }

  protected Object doFieldExtract(SolrDocument document) {
    Slot[] slots = converter.getSlots();
    Object[] documentValueSlots = new Object[slots.length];
    for (int i = 0; i < documentValueSlots.length; i++) {
      Slot slot = slots[i];
      Object documentValueSlot = getSingle(document, slot);
      documentValueSlots[i] = documentValueSlot;
    }
    Object value = converter.toBeanValue(documentValueSlots);
    return value;
  }
}