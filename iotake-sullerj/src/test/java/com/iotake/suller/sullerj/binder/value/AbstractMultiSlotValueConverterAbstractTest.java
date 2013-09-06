package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.value.AbstractMultiSlotValueConverter;

public abstract class AbstractMultiSlotValueConverterAbstractTest extends
    MultiSlotValueConverterAbstractTest {

  protected abstract AbstractMultiSlotValueConverter createInstance();

  @Test
  public void getSlotsNames() {
    AbstractMultiSlotValueConverter converter = createInstance();
    String[] slotNames = converter.getSlotNames();
    assertNotNull(slotNames);
    for (String slotName : slotNames) {
      assertNotNull(slotName);
    }
  }

  @Test
  public void getSlotsTypes() {
    AbstractMultiSlotValueConverter converter = createInstance();
    Class<?>[] slotsTypes = converter.getSlotTypes();
    assertNotNull(slotsTypes);
    for (Class<?> slotsType : slotsTypes) {
      assertNotNull(slotsType);
    }
  }

  @Test
  public void getSlotsNamesAndSlotTypesSameLength() {
    AbstractMultiSlotValueConverter converter = createInstance();
    String[] slotNames = converter.getSlotNames();
    Class<?>[] slotsTypes = converter.getSlotTypes();
    assertNotNull(slotNames);
    assertNotNull(slotsTypes);
    assertEquals(slotNames.length, slotsTypes.length);
  }
}
