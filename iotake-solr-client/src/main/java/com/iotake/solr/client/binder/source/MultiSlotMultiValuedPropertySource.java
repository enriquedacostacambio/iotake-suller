package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.solr.client.binder.value.MultiSlotValueConverter;

public class MultiSlotMultiValuedPropertySource extends
    MultiValuedPropertySource {

  private final MultiSlotValueConverter converter;

  public MultiSlotMultiValuedPropertySource(Class<?> documentClass,
      Field field, boolean readable, boolean writable,
      boolean nullTrickEnabled, MultiSlotValueConverter converter) {
    super(documentClass, field, readable, writable, nullTrickEnabled);
    this.converter = converter;
  }

  public void transferEmpty(SolrInputDocument document) {
    Slot[] slots = converter.getSlots();
    for (Slot slot : slots) {
      setEmpty(document, slot);
    }
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    Slot[] slots = converter.getSlots();
    Object documentValue = converter.toDocumentValues(value);
    Object[] documentValueSlots = (Object[]) documentValue;
    if (documentValueSlots.length != slots.length) {
      throw new BindingException(
          "Invalid number of document values returned from converter for "
              + getDocumentClass().getName() + "." + getField().getName());
    }
    for (int i = 0; i < documentValueSlots.length; i++) {
      Object documentValueSlot = documentValueSlots[i];
      Slot slot = slots[i];
      add(document, slot, documentValueSlot);
    }
  }

  protected Object doFieldExtract(SolrDocument document) {
    Slot[] slots = converter.getSlots();
    Iterator<?>[] iterators = new Iterator[slots.length];
    int items = 0;
    for (int i = 0; i < slots.length; i++) {
      Slot slot = slots[i];
      Collection<?> collection = getCollection(document, slot);
      if (i == 0) {
        items = collection == null ? 0 : collection.size();
      }
      iterators[i] = collection == null ? null : collection.iterator();
    }
    Collection<Object> values = new ArrayList<Object>(items);
    for (int j = 0; j < items; j++) {
      Object[] documentValueSlots = new Object[slots.length];
      for (int i = 0; i < slots.length; i++) {
        Slot slot = slots[i];
        Iterator<?> iterator = iterators[i];
        if (iterator == null || !iterator.hasNext()) {
          throw new BindingException(
              "Incorrect number of collection values for "
                  + getDocumentClass().getName() + "." + getField().getName()
                  + " using path " + slot.getPath());
        }
        Object documentValueSlot = iterator.next();
        documentValueSlots[i] = documentValueSlot;
      }
      Object value = converter.toBeanValue(documentValueSlots);
      values.add(value);
    }
    return values;
  }
}