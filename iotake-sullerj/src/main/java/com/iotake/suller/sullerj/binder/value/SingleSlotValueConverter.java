package com.iotake.suller.sullerj.binder.value;

import com.iotake.suller.sullerj.binder.source.Slot;

public interface SingleSlotValueConverter extends ValueConverter {

  Slot getSlot();

  Object toDocumentValue(Object beanValue);

  Object toBeanValue(Object documentValue);

}