package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.solr.client.binder.value.SingleSlotValueConverter;

public class SingleSlotNotMultiValuedPropertySource extends
    NotMultiValuedPropertySource {

  private final SingleSlotValueConverter converter;

  public SingleSlotNotMultiValuedPropertySource(Class<?> documentClass,
      Field field, boolean readable, boolean writable,
      SingleSlotValueConverter converter) {
    super(documentClass, field, readable, writable);
    this.converter = converter;
  }

  @Override
  public Slot[] getSlots() {
    return new Slot[] { converter.getSlot() };
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    Object documentValue = converter.toDocumentValue(value);
    set(document, converter.getSlot(), documentValue);
  }

  protected Object doFieldExtract(SolrDocument document) {
    Object documentValue = getSingle(document, converter.getSlot());
    Object value = converter.toBeanValue(documentValue);
    return value;
  }
}