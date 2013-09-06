package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.Slot;
import com.iotake.suller.sullerj.binder.value.MultiSlotValueConverter;

public abstract class MultiSlotValueConverterAbstractTest {

  protected abstract MultiSlotValueConverter createInstance();

  @Test
  public void construct() {
    createInstance();
  }

  protected Slot[] validatedGetSlots() {
    MultiSlotValueConverter conveter = createInstance();
    Slot[] slots = conveter.getSlots();
    assertNotNull(slots);
    for (Slot slot : slots) {
      assertNotNull(slot);
      assertNotNull(slot.getPath());
      assertNotNull(slot.getType());
    }
    return slots;
  }

  @SuppressWarnings("unchecked")
  protected Object[] validatedToDocumentValues(Object beanValue) {
    MultiSlotValueConverter conveter = createInstance();
    Object[] documentValues = conveter.toDocumentValues(beanValue);
    assertNotNull(documentValues);
    Slot[] slots = conveter.getSlots();
    int slotCount = slots.length;
    assertEquals(slotCount, documentValues.length);
    for (int i = 0; i < slots.length; i++) {
      Object documentValue = documentValues[i];
      Slot slot = slots[i];
      assertThat(
          documentValue,
          CoreMatchers.anyOf(CoreMatchers.nullValue(),
              CoreMatchers.instanceOf(slot.getType())));
    }
    return documentValues;
  }

  @SuppressWarnings("unchecked")
  protected Object validatedToBeanValue(Object[] documentValues, Class<?> target) {
    MultiSlotValueConverter conveter = createInstance();
    Object beanValue = conveter.toBeanValue(documentValues);
    assertThat(
        beanValue,
        CoreMatchers.anyOf(CoreMatchers.nullValue(),
            CoreMatchers.instanceOf(target)));
    return beanValue;
  }

  @Test(expected = NullPointerException.class)
  public void toBeanValueWithNullDocumentValues() {
    MultiSlotValueConverter conveter = createInstance();
    conveter.toBeanValue(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toBeanValueWithFewerDocumentValues() {
    MultiSlotValueConverter conveter = createInstance();
    Slot[] slots = conveter.getSlots();
    int slotCount = slots.length;
    if (slotCount == 0) {
      // cannot test
      throw new IllegalArgumentException();
    }
    Object[] documentValues = new Object[slotCount - 1];
    conveter.toBeanValue(documentValues);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toBeanValueWithExceededDocumentValues() {
    MultiSlotValueConverter conveter = createInstance();
    Slot[] slots = conveter.getSlots();
    int slotCount = slots.length;
    Object[] documentValues = new Object[slotCount + 1];
    conveter.toBeanValue(documentValues);
  }
}
