package com.iotake.solr.client.binder.value;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Field;

import com.iotake.solr.client.binder.source.Slot;

public abstract class AbstractMultiSlotValueConverter extends
    AbstractValueConverter implements MultiSlotValueConverter {

  private final Slot[] slots;

  public AbstractMultiSlotValueConverter(Class<?> targetRequiredType,
      Class<?> targetType, Class<?> documentClass, Field field,
      String basePath, String slotNameSeparator) {
    super(targetRequiredType, targetType, documentClass, field, basePath);
    checkNotNull(slotNameSeparator, "Slot name separator cannot be null.");
    String[] slotNames = getSlotNames();
    checkNotNull(slotNames, "Slot names array cannot be null.");
    Class<?>[] slotTypes = getSlotTypes();
    checkNotNull(slotTypes, "Slot types array cannot be null.");
    checkState(slotNames.length == slotTypes.length,
        "Slot names and slot types arrays must have the same length: names: "
            + slotNames.length + " types" + slotTypes.length);
    Slot[] slots = new Slot[slotTypes.length];
    for (int i = 0; i < slots.length; i++) {
      String slotName = slotNames[i];
      checkNotNull(slotName, "Slot name cannotbe null.");
      Class<?> slotType = slotTypes[i];
      checkNotNull(slotType, "Slot type cannotbe null.");
      slots[i] = new Slot(basePath + slotNameSeparator + slotName, slotType);
    }
    this.slots = slots;
  }

  protected abstract Class<?>[] getSlotTypes();

  protected abstract String[] getSlotNames();

  public Object toBeanValue(Object[] documentValues) {
    checkNotNull(documentValues, "Document values array cannot be null.");
    checkArgument(slots.length == documentValues.length,
        "Invalid document values array length: expected: " + slots.length
            + ", found: " + documentValues.length);
    return doToBeanValue(documentValues);
  }

  protected abstract Object doToBeanValue(Object[] documentValues);

  public Slot[] getSlots() {
    return slots;
  }

}