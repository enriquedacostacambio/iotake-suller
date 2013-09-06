package com.iotake.suller.sullerj.binder.value;

import java.lang.reflect.Field;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.ValueConverterFactory;

public abstract class ValueConverterFactoryAbstractTest {

  protected abstract ValueConverterFactory createInstance();

  @Test
  public void contruct() {
    createInstance();
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullTargetType() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = null;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullDocumentClass() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = String.class;
    Class<?> documentClass = null;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullField() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = String.class;
    Class<?> documentClass = TestDocument.class;
    Field field = null;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullPath() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = String.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = null;
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullSlotNameSeparator() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = String.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.ID_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = null;
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

}
