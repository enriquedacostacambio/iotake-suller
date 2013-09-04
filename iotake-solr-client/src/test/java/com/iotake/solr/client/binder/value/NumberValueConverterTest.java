package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;

public class NumberValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new NumberValueConverterFactory.NumberValueConverter(Number.class,
        TestDocument.class, TestDocument.NUMBER_FIELD, "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(421D, Number.class);
    assertEquals(421D, beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, Number.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], Number.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(421L);
    assertEquals(421D, documentValue);
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
