package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.util.TestDocument;

public class StringifiableValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  public static class NoValueOf {

  }

  public static class NoPublicValueOf {
    protected static NoPublicValueOf valueOf(String string) {
      return new NoPublicValueOf();
    }
  }

  public static class NoStaticValueOf {
    public NoPublicValueOf valueOf(String string) {
      return new NoPublicValueOf();
    }
  }

  public static class BadReturnTypeValueOf {
    public static Double valueOf(String string) {
      return Double.valueOf(string);
    }
  }

  public static class ExtendedTestDocument extends TestDocument {

    public static final Field NO_VALUE_OF_FIELD = getField("noValueOfField");

    @SolrId
    private NoValueOf noValueOfField;

    public static final Field NO_PUBLIC_VALUE_OF_FIELD = getField("noPublicValueOfField");

    @SolrId
    private NoPublicValueOf noPublicValueOfField;

    public static final Field NO_STATIC_VALUE_OF_FIELD = getField("noStaticValueOfField");

    @SolrId
    private NoStaticValueOf noStaticValueOfField;

    public static final Field BAD_RETURN_TYPE_VALUE_OF_FIELD = getField("badReturnTypeValueOfField");

    @SolrId
    private BadReturnTypeValueOf badReturnTypeValueOfField;

    private static Field getField(String fieldName) {
      return getField(ExtendedTestDocument.class, fieldName);
    }

  }

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return new StringifiableValueConverterFactory.StringifiableValueConverter(
        Long.class, TestDocument.class, TestDocument.ID_FIELD,
        "path__to__field");
  }

  @Test(expected = BindingException.class)
  public void createInstanceWithNoValueOf() {
    new StringifiableValueConverterFactory.StringifiableValueConverter(
        NoValueOf.class, ExtendedTestDocument.class,
        ExtendedTestDocument.NO_VALUE_OF_FIELD, "path__to__field");
  }

  @Test(expected = BindingException.class)
  public void createInstanceWithNoPublicValueOf() {
    new StringifiableValueConverterFactory.StringifiableValueConverter(
        NoPublicValueOf.class, ExtendedTestDocument.class,
        ExtendedTestDocument.NO_PUBLIC_VALUE_OF_FIELD, "path__to__field");
  }

  @Test(expected = BindingException.class)
  public void createInstanceWithNoStaticValueOf() {
    new StringifiableValueConverterFactory.StringifiableValueConverter(
        NoStaticValueOf.class, ExtendedTestDocument.class,
        ExtendedTestDocument.NO_STATIC_VALUE_OF_FIELD, "path__to__field");
  }

  @Test(expected = BindingException.class)
  public void createInstanceWithBadReturnTypeValueOf() {
    new StringifiableValueConverterFactory.StringifiableValueConverter(
        BadReturnTypeValueOf.class, ExtendedTestDocument.class,
        ExtendedTestDocument.BAD_RETURN_TYPE_VALUE_OF_FIELD, "path__to__field");
  }

  @Test
  public void toBeanValue() {
    Object beanValue = validatedToBeanValue("421", Long.class);
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

  @Test(expected = BindingException.class)
  public void toBeanValueWithInvalidValue() {
    validatedToBeanValue("x", Long.class);
  }

  @Test
  public void toDocumentValue() {
    Object documentValue = validatedToDocumentValue(421L);
    assertEquals("421", documentValue);
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
