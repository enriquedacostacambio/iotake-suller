package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.source.Slot;

public abstract class SingleSlotValueConverterAbstractTest {

  protected abstract SingleSlotValueConverter createInstance();

  @Test
  public void construct() {
    createInstance();
  }

  protected Slot validatedGetSlot() {
    SingleSlotValueConverter conveter = createInstance();
    Slot slot = conveter.getSlot();
    assertNotNull(slot);
    assertNotNull(slot.getPath());
    assertNotNull(slot.getType());
    return slot;
  }

  @SuppressWarnings("unchecked")
  protected Object validatedToDocumentValue(Object beanValue) {
    SingleSlotValueConverter conveter = createInstance();
    Object documentValue = conveter.toDocumentValue(beanValue);
    Slot slot = conveter.getSlot();
    assertThat(
        documentValue,
        CoreMatchers.anyOf(CoreMatchers.<Object> nullValue(),
            CoreMatchers.instanceOf(slot.getWrapperType())));
    return documentValue;
  }

  @SuppressWarnings("unchecked")
  protected Object validatedToBeanValue(Object documentValue, Class<?> target) {
    SingleSlotValueConverter conveter = createInstance();
    Object beanValue = conveter.toBeanValue(documentValue);
    assertThat(
        beanValue,
        CoreMatchers.anyOf(CoreMatchers.<Object> nullValue(),
            CoreMatchers.instanceOf(target)));
    return beanValue;
  }

}
