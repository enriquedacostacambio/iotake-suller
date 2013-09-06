package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.BigIntegerValueConverterFactory;

public class BigIntegerValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new BigIntegerValueConverterFactory.BigIntegerValueConverter(
        BigInteger.class, TestDocument.class, TestDocument.ID_FIELD,
        "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue("431", BigInteger.class);
    assertEquals(BigInteger.valueOf(431), beanValue);
  }

  @Test
  public void toBeanValueWithNull() {
    Object beanValue = validatedToBeanValue(null, BigInteger.class);
    assertNull(beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], BigInteger.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValue() {
    validatedToBeanValue("x", BigInteger.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(BigInteger.valueOf(431));
    assertEquals("431", documentValue);
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
