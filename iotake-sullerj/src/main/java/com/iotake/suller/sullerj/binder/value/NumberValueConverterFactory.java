package com.iotake.suller.sullerj.binder.value;

import java.lang.reflect.Field;

public class NumberValueConverterFactory extends AbstractValueConverterFactory {

  public static class NumberValueConverter extends
      AbstractSingleSlotValueConverter {

    public NumberValueConverter(Class<?> targetType, Class<?> documentClass,
        Field field, String path) {
      super(Number.class, targetType, documentClass, field, path);
    }

    public Class<?> getSlotType() {
      return Double.class;
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      return ((Number) beanValue).doubleValue();

    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      return documentValue;
    }
  }

  public NumberValueConverterFactory() {
    super(Number.class);
  }

  protected ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    return new NumberValueConverter(targetType, documentClass, field, slotName);
  }
}