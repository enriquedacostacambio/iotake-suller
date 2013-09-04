package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;

public class OrdinalEnumValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new EnumValueConverterFactory.OrdinalEnumValueConverter(TimeUnit.class,
        TestDocument.class, TestDocument.ORDINAL_ENUM_FIELD, "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(3, TimeUnit.class);
    assertEquals(TimeUnit.SECONDS, beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, TimeUnit.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue("x", TimeUnit.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValue() {
    validatedToBeanValue(4199, TimeUnit.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(TimeUnit.SECONDS);
    assertEquals(3, documentValue);
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
