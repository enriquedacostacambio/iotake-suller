package com.iotake.suller.sullerj.binder.value;

import java.lang.reflect.Field;

import com.iotake.suller.sullerj.binder.source.Slot;

public class NativeValueConverterFactory extends AbstractValueConverterFactory {

  public static class NullableNativeValueConverter extends
      AbstractSingleSlotValueConverter {

    public NullableNativeValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(Anything.class, targetType, documentClass, field, path);
    }

    public Class<?> getSlotType() {
      return getTargetType();
    }

    public Object toDocumentValue(Object beanValue) {
      checkBeanValueType(beanValue);
      return beanValue;
    }

    public Object toBeanValue(Object documentValue) {
      checkDocumentValueType(documentValue, getSlot());
      return documentValue;
    }
  };

  public static class NotNullableNativeValueConverter extends
      AbstractSingleSlotValueConverter {

    public NotNullableNativeValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(targetType, targetType, documentClass, field, path);
    }

    public Class<?> getSlotType() {
      return getTargetType();
    }

    public Object toDocumentValue(Object beanValue) {
      checkNonNullBeanValue(beanValue);
      checkBeanValueType(beanValue);
      return beanValue;
    }

    public Object toBeanValue(Object documentValue) {
      Slot slot = getSlot();
      checkNonNullDocumentValue(documentValue, slot);
      checkDocumentValueType(documentValue, slot);
      return documentValue;
    }
  };

  public NativeValueConverterFactory() {
    super(Anything.class);
  }

  protected ValueConverter doCreate(java.lang.Class<?> targetType,
      java.lang.Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    if (targetType.isPrimitive()) {
      return new NotNullableNativeValueConverter(targetType, documentClass,
          field, slotName);
    } else {
      return new NullableNativeValueConverter(targetType, documentClass, field,
          slotName);
    }
  }
}