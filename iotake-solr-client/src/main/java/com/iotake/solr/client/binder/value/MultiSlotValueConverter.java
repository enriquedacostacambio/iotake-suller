package com.iotake.solr.client.binder.value;

import com.iotake.solr.client.binder.source.Slot;

public interface MultiSlotValueConverter extends ValueConverter {

  Slot[] getSlots();

  Object[] toDocumentValues(Object beanValue);

  Object toBeanValue(Object[] documentValues);

}