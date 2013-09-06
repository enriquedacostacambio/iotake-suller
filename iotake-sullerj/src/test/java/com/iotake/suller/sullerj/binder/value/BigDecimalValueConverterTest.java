package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.BigDecimalValueConverterFactory;

public class BigDecimalValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new BigDecimalValueConverterFactory.BigDecimalValueConverter(
        BigDecimal.class, TestDocument.class, TestDocument.ID_FIELD,
        "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue("431", BigDecimal.class);
    assertEquals(BigDecimal.valueOf(431), beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, BigDecimal.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], BigDecimal.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValue() {
    validatedToBeanValue("x", BigDecimal.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(BigDecimal.valueOf(431, 1));
    assertEquals("43.1", documentValue);
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
