package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.NativeValueConverterFactory.NotNullableNativeValueConverter;
import com.iotake.solr.client.binder.value.NativeValueConverterFactory.NullableNativeValueConverter;

public class NativeValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected NativeValueConverterFactory createInstance() {
    return new NativeValueConverterFactory();
  }

  @Test
  public void doCreateWithNotNullable() {
    NativeValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(double.class,
        TestDocument.class, TestDocument.DOUBLE_FIELD, "path__to__field", "__");
    assertThat(converter,
        CoreMatchers.instanceOf(NotNullableNativeValueConverter.class));
  }

  @Test
  public void doCreateWithNullable() {
    NativeValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(Long.class,
        TestDocument.class, TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter,
        CoreMatchers.instanceOf(NullableNativeValueConverter.class));
  }

}
