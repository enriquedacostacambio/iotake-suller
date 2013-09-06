package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.Slot;
import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.util.TestDocument.ComplexValue;
import com.iotake.suller.sullerj.binder.value.AbstractMultiSlotValueConverter;

public class AbstractMultiSlotValueConverterTest extends
    AbstractMultiSlotValueConverterAbstractTest {

  @Override
  protected AbstractMultiSlotValueConverter createInstance() {
    return createInstance(new String[] { "a", "b" }, new Class<?>[] {
        Integer.class, String.class });
  }

  protected AbstractMultiSlotValueConverter createInstance(
      Class<?> targetRequiredType, Class<?> targetType, Class<?> documentClass,
      Field field, String path, String slotNameSeparator) {
    return createInstance(targetRequiredType, targetType, documentClass, field,
        path, slotNameSeparator, new String[] { "a", "b" }, new Class<?>[] {
            Integer.class, String.class });
  }

  protected AbstractMultiSlotValueConverter createInstance(String[] slotNames,
      Class<?>[] slotTypes) {
    Class<?> targetRequiredType = ComplexValue.class;
    Class<?> targetType = ComplexValue.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.COMPLEX_VALUE_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    return createInstance(targetRequiredType, targetType, documentClass, field,
        path, slotNameSeparator, slotNames, slotTypes);
  }

  protected AbstractMultiSlotValueConverter createInstance(
      Class<?> targetRequiredType, Class<?> targetType, Class<?> documentClass,
      Field field, String path, String slotNameSeparator,
      final String[] slotNames, final Class<?>[] slotTypes) {
    return new AbstractMultiSlotValueConverter(targetRequiredType, targetType,
        documentClass, field, path, slotNameSeparator) {

      @Override
      protected String[] getSlotNames() {
        return slotNames;
      }

      @Override
      protected Class<?>[] getSlotTypes() {
        return slotTypes;
      }

      public Object doToBeanValue(Object[] documentValues) {
        throw new UnsupportedOperationException();
      }

      public Object[] toDocumentValues(Object beanValue) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullSlotNameSeparator() {
    Class<?> targetRequiredType = ComplexValue.class;
    Class<?> targetType = ComplexValue.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.COMPLEX_VALUE_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = null;
    createInstance(targetRequiredType, targetType, documentClass, field, path,
        slotNameSeparator);
  }

  @Test(expected = NullPointerException.class)
  public void getSlotNamesReturnsNull() {
    createInstance(null, new Class<?>[] { Integer.class, String.class });
  }

  @Test(expected = NullPointerException.class)
  public void getSlotNamesReturnsNullElement() {
    createInstance(new String[] { "a", null }, new Class<?>[] { Integer.class,
        String.class });
  }

  @Test(expected = NullPointerException.class)
  public void getSlotTypesReturnsNull() {
    createInstance(new String[] { "a", "b" }, null);
  }

  @Test(expected = NullPointerException.class)
  public void getSlotTypesReturnsNullElement() {
    createInstance(new String[] { "a", "b" }, new Class<?>[] { Integer.class,
        null });
  }

  @Test(expected = IllegalStateException.class)
  public void getSlotNamesAndGetSlotTypesLengthDontMatch() {
    createInstance(new String[] { "a", "b", "c" }, new Class<?>[] {
        Integer.class, String.class });
  }

  @Test
  public void getSlots() {
    Class<?> targetRequiredType = ComplexValue.class;
    Class<?> targetType = ComplexValue.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.COMPLEX_VALUE_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    AbstractMultiSlotValueConverter converter = createInstance(
        targetRequiredType, targetType, documentClass, field, path,
        slotNameSeparator, new String[] { "a", "b" }, new Class<?>[] {
            Integer.class, String.class });
    Slot[] slots = converter.getSlots();
    assertNotNull(slots);
    assertEquals(2, slots.length);
    assertNotNull(slots[0]);
    assertNotNull(slots[1]);
    assertEquals(path + slotNameSeparator + "a", slots[0].getPath());
    assertEquals(path + slotNameSeparator + "b", slots[1].getPath());
    assertEquals(Integer.class, slots[0].getType());
    assertEquals(String.class, slots[1].getType());
  }

}
