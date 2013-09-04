package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;

public class NotNullableNativeValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new NativeValueConverterFactory.NotNullableNativeValueConverter(
        double.class, TestDocument.class, TestDocument.DOUBLE_FIELD,
        "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue(421.3D, Double.class);
    assertEquals(421.3D, beanValue);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithNull() {
    validatedToBeanValue(null, Double.class);
  }

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidType() {
    validatedToBeanValue(new int[1], Double.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(421.3D);
    assertEquals(421.3D, documentValue);
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
