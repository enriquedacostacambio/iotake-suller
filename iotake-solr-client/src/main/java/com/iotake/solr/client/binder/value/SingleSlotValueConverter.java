package com.iotake.solr.client.binder.value;

import com.iotake.solr.client.binder.source.Slot;

public interface SingleSlotValueConverter extends ValueConverter {

  Slot getSlot();

  Object toDocumentValue(Object beanValue);

  Object toBeanValue(Object documentValue);

}