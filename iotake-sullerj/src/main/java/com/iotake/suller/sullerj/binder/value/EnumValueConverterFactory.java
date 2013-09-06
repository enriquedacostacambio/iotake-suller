package com.iotake.suller.sullerj.binder.value;

import java.lang.reflect.Field;

import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated.EnumType;

public class EnumValueConverterFactory extends AbstractValueConverterFactory {

  public static class NameEnumValueConverter extends
      AbstractSingleSlotValueConverter {

    public NameEnumValueConverter(Class<?> targetType, Class<?> documentClass,
        Field field, String path) {
      super(Enum.class, targetType, documentClass, field, path);
    }

    public Class<?> getSlotType() {
      return String.class;
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T getValueOf(String documentValue) {
      return (T) Enum.valueOf((Class<T>) getTargetType(), documentValue);
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      return ((Enum<?>) beanValue).name();

    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      try {
        return getValueOf((String) documentValue);
      } catch (IllegalArgumentException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Enum name is unknown.", e);
      }
    }
  }

  public static class OrdinalEnumValueConverter extends
      AbstractSingleSlotValueConverter {

    private final Object[] enumConstants;

    public OrdinalEnumValueConverter(Class<?> targetType,
        Class<?> documentClass, Field field, String path) {
      super(Enum.class, targetType, documentClass, field, path);
      enumConstants = targetType.getEnumConstants();
    }

    public Class<?> getSlotType() {
      return Integer.class;
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      return ((Enum<?>) beanValue).ordinal();

    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      int index = ((Integer) documentValue).intValue();
      if (index < 0 || index >= enumConstants.length) {
        throw reportInvalidDocumentValue(documentValue, getSlot(), "Invalid ordinal.");
      }
      return enumConstants[index];
    }
  }

  public EnumValueConverterFactory() {
    super(Enum.class);
  }

  protected ValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName, String slotNameSeparator) {
    SolrEnumerated solrEnumerated = field.getAnnotation(SolrEnumerated.class);
    if (solrEnumerated == null || solrEnumerated.value() == EnumType.NAME) {
      return new NameEnumValueConverter(targetType, documentClass, field,
          slotName);
    } else {
      return new OrdinalEnumValueConverter(targetType, documentClass, field,
          slotName);
    }
  }
}