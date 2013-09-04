package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class BigDecimalValueConverterFactory extends
    AbstractValueConverterFactory {

  public static class BigDecimalValueConverter extends
      AbstractSingleSlotValueConverter {

    public BigDecimalValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(BigDecimal.class, targetType, documentClass, field, path);
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
        return new BigDecimal((String) documentValue);
      } catch (NumberFormatException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Invalid BigInteger string format.", e);
      }
    }
  }

  public BigDecimalValueConverterFactory() {
    super(BigDecimal.class);
  }

  protected ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    return new BigDecimalValueConverter(targetType, documentClass, field,
        slotName);
  }
}