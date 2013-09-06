package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;

import com.iotake.solr.client.binder.source.Slot;

public class GlobalIdValueConverterFactory extends
    AbstractValueConverterFactory {

  public static class GlobalIdValueConverter extends
      AbstractSingleSlotValueConverter {

    public static final char DELIMITER = '@';

    private final String idPrefix;
    private final SingleSlotValueConverter converter;

    public GlobalIdValueConverter(Class<?> targetType, Class<?> documentClass,
        Field field, String path, String slotNameSeparator) {
      super(Anything.class, targetType, documentClass, field, path);
      idPrefix = documentClass.getName() + DELIMITER;
      ValueConverterFactory converterFactory = new StringifiableValueConverterFactory();
      converter = (SingleSlotValueConverter) converterFactory.create(
          field.getType(), documentClass, field, path, slotNameSeparator);
    }

    public Class<?> getSlotType() {
      return String.class;
    }

    public Object toDocumentValue(Object beanValue) {
      checkNonNullBeanValue(beanValue);
      checkBeanValueType(beanValue);

      String id = (String) converter.toDocumentValue(beanValue);
      String globalId = idPrefix + id;

      return globalId;
    }

    public Object toBeanValue(Object documentValue) {
      Slot slot = getSlot();
      checkNonNullDocumentValue(documentValue, slot);
      checkDocumentValueType(documentValue, slot);

      String globalId = (String) documentValue;
      if (!globalId.startsWith(idPrefix)) {
        throw reportInvalidDocumentValue(documentValue, slot,
            "Invalid id, expecting id with format " + idPrefix + "xxx");
      }
      String id = globalId.substring(idPrefix.length());
      Object beanValue = converter.toBeanValue(id);
      return beanValue;
    }
  }

  public GlobalIdValueConverterFactory() {
    super(Anything.class);
  }

  protected GlobalIdValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    return new GlobalIdValueConverter(targetType, documentClass, field,
        slotName, slotNameSeparator);
  }
}