package com.iotake.suller.sullerj.binder.value;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;

import com.iotake.suller.sullerj.binder.source.Slot;

public abstract class AbstractSingleSlotValueConverter extends
    AbstractValueConverter implements SingleSlotValueConverter {

  private final Slot slot;

  public AbstractSingleSlotValueConverter(Class<?> targetRequiredType,
      Class<?> targetType, Class<?> documentClass, Field field, String path) {
    super(targetRequiredType, targetType, documentClass, field, path);
    Class<?> slotType  = getSlotType();
    checkNotNull(slotType);
    this.slot = new Slot(path, slotType);
  }

  protected abstract Class<?> getSlotType();

  public Slot getSlot() {
    return slot;
  }

}