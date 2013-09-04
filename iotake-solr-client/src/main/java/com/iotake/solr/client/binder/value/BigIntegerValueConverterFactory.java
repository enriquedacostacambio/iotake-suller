package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;
import java.math.BigInteger;

public class BigIntegerValueConverterFactory extends
    AbstractValueConverterFactory {

  public static class BigIntegerValueConverter extends
      AbstractSingleSlotValueConverter {

    public BigIntegerValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(BigInteger.class, targetType, documentClass, field, path);
    }

    public Class<?> getSlotType() {
      return String.class;
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      return beanValue.toString();

    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      try {
        return new BigInteger((String) documentValue);
      } catch (NumberFormatException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Invalid BigInteger string format.", e);
      }
    }
  }

  public BigIntegerValueConverterFactory() {
    super(BigInteger.class);
  }

  protected ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    return new BigIntegerValueConverter(targetType, documentClass, field,
        slotName);
  }
}