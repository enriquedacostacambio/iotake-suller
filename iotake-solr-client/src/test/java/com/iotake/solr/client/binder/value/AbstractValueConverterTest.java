package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.solr.client.binder.source.Slot;
import com.iotake.solr.client.binder.util.TestDocument;

public class AbstractValueConverterTest {

  protected AbstractValueConverter createInstance() {
    return createInstance(String.class, String.class, TestDocument.class,
        TestDocument.STRING_FIELD, "path__to__field");
  }

  protected AbstractValueConverter createInstance(Class<?> targetRequiredType,
      Class<?> targetType, Class<?> documentClass, Field field, String path) {
    return new AbstractValueConverter(targetRequiredType, targetType,
        documentClass, field, path) {
    };
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullTargetImplementationType() {
    Class<?> targetRequiredType = null;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = null;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructWithImplementedTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Serializable.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.CLASS_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructWithExtendedTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Number.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.NUMBER_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructWithNotImplementedTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Closeable.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructWithNotExtendedTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = String.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.STRING_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test
  public void constructWithExtendingTargetType() {
    Class<?> targetRequiredType = Number.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullDocumentClass() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = null;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullField() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = null;
    String path = "path__to__field";
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullPath() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = null;
    createInstance(targetRequiredType, targetType, documentClass, field, path);
  }

  @Test
  public void getTargetType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    AbstractValueConverter converter = createInstance(targetRequiredType,
        targetType, documentClass, field, path);
    assertSame(Long.class, converter.getTargetType());
  }
  
  @Test
  public void getTargetWrapperType() {
    Class<?> targetRequiredType = Long.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    AbstractValueConverter converter = createInstance(targetRequiredType,
        targetType, documentClass, field, path);
    assertSame(Long.class, converter.getTargetWrapperType());
  }
  
  @Test
  public void getTargetTypeWithPrimitive() {
    Class<?> targetRequiredType = double.class;
    Class<?> targetType = double.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.DOUBLE_FIELD;
    String path = "path__to__field";
    AbstractValueConverter converter = createInstance(targetRequiredType,
        targetType, documentClass, field, path);
    assertSame(double.class, converter.getTargetType());
  }
  
  @Test
  public void getTargetWrapperTypeWithPrimitive() {
    Class<?> targetRequiredType = double.class;
    Class<?> targetType = double.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.DOUBLE_FIELD;
    String path = "path__to__field";
    AbstractValueConverter converter = createInstance(targetRequiredType,
        targetType, documentClass, field, path);
    assertSame(Double.class, converter.getTargetWrapperType());
  }

  @Test
  public void getDocumentClass() {
    AbstractValueConverter converter = createInstance();
    assertSame(TestDocument.class, converter.getDocumentClass());
  }

  @Test
  public void getField() {
    AbstractValueConverter converter = createInstance();
    assertSame(TestDocument.STRING_FIELD,
        converter.getField());
  }

  @Test
  public void getPath() {
    AbstractValueConverter converter = createInstance();
    assertEquals("path__to__field", converter.getPath());
  }

  @Test
  public void checkBeanValueType() {
    AbstractValueConverter converter = createInstance();
    converter.checkBeanValueType("test-bean-value");
  }

  @Test
  public void checkBeanValueTypeWithNullBeanValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkBeanValueType(null);
  }

  @Test(expected = BindingException.class)
  public void checkBeanValueTypeInvalidBeanValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkBeanValueType(Double.valueOf(12345));
  }

  @Test
  public void checkBeanValueTypeWithExtendedBeanValue() {
    Class<?> targetRequiredType = Number.class;
    Class<?> targetType = Long.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.NUMBER_FIELD;
    String path = "path__to__field";
    AbstractValueConverter converter = createInstance(targetRequiredType,
        targetType, documentClass, field, path);
    converter.checkBeanValueType(Long.valueOf(12345));
  }

  @Test
  public void reportInvalidDocumentValue() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidDocumentValue("aValue",
        new Slot("path__to__field", String.class), "A reason");
    assertNotNull(exception);
  }

  @Test(expected = NullPointerException.class)
  public void reportInvalidDocumentValueWithNullSlot() {
    AbstractValueConverter converter = createInstance();
    converter.reportInvalidDocumentValue("aValue", null, "A reason");
  }

  @Test(expected = NullPointerException.class)
  public void reportInvalidDocumentValueWithNullReason() {
    AbstractValueConverter converter = createInstance();
    converter.reportInvalidDocumentValue("aValue", new Slot("path__to__field",
        String.class), null);
  }

  @Test
  public void reportInvalidDocumentValueWithException() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidDocumentValue("aValue",
        new Slot("path__to__field", String.class), "A reason", new Exception(
            "An exception"));
    assertNotNull(exception);
  }

  @Test
  public void reportInvalidDocumentValueWithNullException() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidDocumentValue("aValue",
        new Slot("path__to__field", String.class), "A reason", null);
    assertNotNull(exception);
  }
  
  @Test
  public void reportInvalidDocumentValueWithNullValue() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidDocumentValue(null,
        new Slot("path__to__field", String.class), "A reason", new Exception(
            "An exception"));
    assertNotNull(exception);
  }

  @Test
  public void reportInvalidBeanValue() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidBeanValue("aValue",
        "A reason");
    assertNotNull(exception);
  }

  @Test(expected = NullPointerException.class)
  public void reportInvalidBeanValueWithNullReason() {
    AbstractValueConverter converter = createInstance();
    converter.reportInvalidBeanValue("aValue", null);
  }

  @Test
  public void reportInvalidBeanValueWithException() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidBeanValue("aValue",
        "A reason", new Exception("An exception"));
    assertNotNull(exception);
  }

  @Test
  public void reportInvalidBeanValueWithNullException() {
    AbstractValueConverter converter = createInstance();
    BindingException exception = converter.reportInvalidBeanValue("aValue",
        "A reason", null);
    assertNotNull(exception);
  }

  @Test
  public void checkNonNullDocumentValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkNonNullDocumentValue("test-bean-value", new Slot(
        "path__to__property", String.class));
  }

  @Test(expected = BindingException.class)
  public void checkNonNullDocumentValueWithNullBeanValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkNonNullDocumentValue(null, new Slot("path__to__property",
        String.class));
  }

  @Test(expected = NullPointerException.class)
  public void checkNonNullDocumentValueWithNullSlot() {
    AbstractValueConverter converter = createInstance();
    converter.checkNonNullDocumentValue("test-bean-value", null);
  }

  @Test
  public void checkNonNullBeanValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkNonNullBeanValue("test-bean-value");
  }

  @Test(expected = BindingException.class)
  public void checkNonNullBeanValueWithNullBeanValue() {
    AbstractValueConverter converter = createInstance();
    converter.checkNonNullBeanValue(null);
  }
}