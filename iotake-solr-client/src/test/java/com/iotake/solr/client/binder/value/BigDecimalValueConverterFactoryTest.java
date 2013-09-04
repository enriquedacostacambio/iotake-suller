package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.BigDecimalValueConverterFactory.BigDecimalValueConverter;

public class BigDecimalValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected BigDecimalValueConverterFactory createInstance() {
    return new BigDecimalValueConverterFactory();
  }

  @Test
  public void doCreate() {
    BigDecimalValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(BigDecimal.class,
        TestDocument.class, TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter, CoreMatchers.instanceOf(BigDecimalValueConverter.class));
  }
}
