package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;

import com.iotake.solr.client.binder.source.Slot;
import com.iotake.solr.client.binder.util.TestDocument;

public class AbstractSingleSlotValueConverterTest extends
    AbstractSingleSlotValueConverterAbstractTest {

  @Override
  protected AbstractSingleSlotValueConverter createInstance() {
    return createInstance(String.class);
  }

  protected AbstractSingleSlotValueConverter createInstance(
      Class<?> targetRequiredType, Class<?> targetType, Class<?> documentClass,
      Field field, String path) {
    return createInstance(targetRequiredType, targetType, documentClass, field,
        path, String.class);
  }

  protected AbstractSingleSlotValueConverter createInstance(Class<?> slotType) {
    Class<?> targetRequiredType = Class.class;
    Class<?> targetType = Class.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.COMPLEX_VALUE_FIELD;
    String path = "path__to__field";
    return createInstance(targetRequiredType, targetType, documentClass, field,
        path, slotType);
  }

  protected AbstractSingleSlotValueConverter createInstance(
      Class<?> targetRequiredType, Class<?> targetType, Class<?> documentClass,
      Field field, String path, final Class<?> slotType) {
    return new AbstractSingleSlotValueConverter(targetRequiredType, targetType,
        documentClass, field, path) {

      @Override
      protected Class<?> getSlotType() {
        return slotType;
      }

      public Object toBeanValue(Object documentValue) {
        throw new UnsupportedOperationException();
      }

      public Object toDocumentValue(Object beanValue) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(expected = NullPointerException.class)
  public void getSlotTypeReturnsNull() {
    createInstance(null);
  }

  @Test
  public void getSlot() {
    Class<?> targetRequiredType = Class.class;
    Class<?> targetType = Class.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.CLASS_FIELD;
    String path = "path__to__field";
    AbstractSingleSlotValueConverter converter = createInstance(
        targetRequiredType, targetType, documentClass, field, path,
        String.class);
    Slot slot = converter.getSlot();
    assertNotNull(slot);
    assertEquals(path, slot.getPath());
    assertEquals(String.class, slot.getType());
  }

}
