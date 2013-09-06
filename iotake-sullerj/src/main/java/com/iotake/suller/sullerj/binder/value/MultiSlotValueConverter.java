package com.iotake.suller.sullerj.binder.value;

import com.iotake.suller.sullerj.binder.source.Slot;

public interface MultiSlotValueConverter extends ValueConverter {

  Slot[] getSlots();

  Object[] toDocumentValues(Object beanValue);

  Object toBeanValue(Object[] documentValues);

}