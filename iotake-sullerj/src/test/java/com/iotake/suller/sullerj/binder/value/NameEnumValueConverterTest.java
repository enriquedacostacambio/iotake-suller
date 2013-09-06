package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.EnumValueConverterFactory;

public class NameEnumValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new EnumValueConverterFactory.NameEnumValueConverter(TimeUnit.class,
        TestDocument.class, TestDocument.NAME_ENUM_FIELD, "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue("SECONDS", TimeUnit.class);
    assertEquals(TimeUnit.SECONDS, beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, TimeUnit.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], TimeUnit.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValue() {
    validatedToBeanValue("x", TimeUnit.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(TimeUnit.SECONDS);
    assertEquals("SECONDS", documentValue);
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
