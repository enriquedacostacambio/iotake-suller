package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.NativeValueConverterFactory.NullableNativeValueConverter;
import com.iotake.solr.client.binder.value.StringifiableValueConverterFactory.StringifiableValueConverter;

public class StringifiableValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected StringifiableValueConverterFactory createInstance() {
    return new StringifiableValueConverterFactory();
  }

  @Test
  public void doCreateWithString() {
    StringifiableValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(String.class,
        TestDocument.class, TestDocument.STRING_FIELD, "path__to__field", "__");
    assertThat(converter,
        CoreMatchers.instanceOf(NullableNativeValueConverter.class));
  }

  @Test
  public void doCreateWithNotString() {
    StringifiableValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(Long.class, TestDocument.class,
        TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter,
        CoreMatchers.instanceOf(StringifiableValueConverter.class));
  }

}
