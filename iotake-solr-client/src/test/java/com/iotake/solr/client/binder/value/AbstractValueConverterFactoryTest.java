package com.iotake.solr.client.binder.value;

import java.lang.reflect.Field;

import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;

public class AbstractValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected AbstractValueConverterFactory createInstance() {
    return new AbstractValueConverterFactory(Number.class) {

      @Override
      protected ValueConverter doCreate(Class<?> targetType,
          Class<?> documentClass, Field field, String slotName,
          String slotNameSeparator) {
        return new ValueConverter() {
        };
      }
    };
  }

  @Test
  public void create() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = Number.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.NUMBER_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test
  public void createWithTargetSubType() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = Double.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.DOUBLE_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createWithInvalidTargetType() {
    ValueConverterFactory factory = createInstance();
    Class<?> targetType = String.class;
    Class<?> documentClass = TestDocument.class;
    Field field = TestDocument.LIST_OF_STRING_FIELD;
    String path = "path__to__field";
    String slotNameSeparator = "__";
    factory.create(targetType, documentClass, field, path, slotNameSeparator);
  }

}
