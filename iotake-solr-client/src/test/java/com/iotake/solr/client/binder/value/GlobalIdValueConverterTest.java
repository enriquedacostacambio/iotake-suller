package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;

public class GlobalIdValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new GlobalIdValueConverterFactory.GlobalIdValueConverter(
        Long.class, TestDocument.class, TestDocument.ID_FIELD,
        "path__to__field", "__");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(TestDocument.class.getName()
        + "@431", Long.class);
    assertEquals(431L, beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithNull() {
    validatedToBeanValue(null, Long.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], Long.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidPrefix() {
    validatedToBeanValue("abc@123", Long.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValuePart() {
    validatedToBeanValue(TestDocument.class.getName() + "@x", Long.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(431L);
    assertEquals(TestDocument.class.getName() + "@431", documentValue);
  }

  @Test(expected = BindingException.class)
  public void toDocumentValueWithNull() {
    validatedToDocumentValue(null);
  }

  @Test(expected = BindingException.class)
  public void toDocumentValueWithInvalidType() {
    validatedToDocumentValue("boo");
  }

}
