package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import java.math.BigInteger;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.BigIntegerValueConverterFactory.BigIntegerValueConverter;

public class BigIntegerValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected BigIntegerValueConverterFactory createInstance() {
    return new BigIntegerValueConverterFactory();
  }

  @Test
  public void doCreate() {
    BigIntegerValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(BigInteger.class,
        TestDocument.class, TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter, CoreMatchers.instanceOf(BigIntegerValueConverter.class));
  }

}
