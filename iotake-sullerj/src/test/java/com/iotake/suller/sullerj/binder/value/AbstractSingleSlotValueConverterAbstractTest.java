package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;

public abstract class AbstractSingleSlotValueConverterAbstractTest extends
    SingleSlotValueConverterAbstractTest {

  protected abstract AbstractSingleSlotValueConverter createInstance();

  @Test
  public void getSlotsType() {
    AbstractSingleSlotValueConverter converter = createInstance();
    Class<?> slotsType = converter.getSlotType();
    assertNotNull(slotsType);
  }

}
