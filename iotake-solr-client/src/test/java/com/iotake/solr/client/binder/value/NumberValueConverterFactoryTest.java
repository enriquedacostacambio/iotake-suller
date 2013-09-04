package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.NumberValueConverterFactory.NumberValueConverter;

public class NumberValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected NumberValueConverterFactory createInstance() {
    return new NumberValueConverterFactory();
  }

  @Test
  public void doCreate() {
    NumberValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(Number.class,
        TestDocument.class, TestDocument.NUMBER_FIELD, "path__to__field", "__");
    assertThat(converter, CoreMatchers.instanceOf(NumberValueConverter.class));
  }

}
