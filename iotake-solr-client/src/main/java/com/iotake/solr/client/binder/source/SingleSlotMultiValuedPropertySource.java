package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.solr.client.binder.value.SingleSlotValueConverter;

public class SingleSlotMultiValuedPropertySource extends
    MultiValuedPropertySource {

  private final SingleSlotValueConverter converter;

  public SingleSlotMultiValuedPropertySource(Class<?> documentClass,
      Field field, String path, boolean readable, boolean writable,
      boolean nullTrickEnabled, SingleSlotValueConverter converter) {
    super(documentClass, field, readable, writable, nullTrickEnabled);
    this.converter = converter;
  }

  @Override
  public Slot[] getSlots() {
    return new Slot[] { converter.getSlot() };
  }

  public void transferEmpty(SolrInputDocument document) {
    setEmpty(document, converter.getSlot());
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    Object documentValue = converter.toDocumentValue(value);
    add(document, converter.getSlot(), documentValue);
  }

  protected Object doFieldExtract(SolrDocument document) {
    Collection<?> documentValues = getCollection(document, converter.getSlot());
    if (documentValues == null) {
      return Collections.emptyList();
    }
    Collection<Object> values = new ArrayList<Object>(documentValues.size());
    for (Object documentValue : documentValues) {
      Object value = converter.toBeanValue(documentValue);
      values.add(value);
    }
    return values;
  }
}