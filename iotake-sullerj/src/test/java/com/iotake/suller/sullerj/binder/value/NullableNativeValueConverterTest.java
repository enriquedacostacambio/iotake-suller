package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.NativeValueConverterFactory;

public class NullableNativeValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new NativeValueConverterFactory.NullableNativeValueConverter(
        Long.class, TestDocument.class, TestDocument.ID_FIELD,
        "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(421L, Long.class);
    assertEquals(421L, beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, Long.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], Long.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(421L);
    assertEquals(421L, documentValue);
  }

  @Test
  public void toDocumentValueWithNull() {
    Object documentValue = validatedToDocumentValue(null);
    assertNull(documentValue);
  }

  @Test(expected = BindingException.class)
  public void toDocumentValueWithInvalidType() {
    validatedToDocumentValue("boo");
  }

}
