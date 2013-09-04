package com.iotake.solr.client.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.GlobalIdValueConverterFactory.GlobalIdValueConverter;

public class GlobalIdValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected GlobalIdValueConverterFactory createInstance() {
    return new GlobalIdValueConverterFactory();
  }

  @Test
  public void doCreate() {
    GlobalIdValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(Integer.class,
        TestDocument.class, TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter, CoreMatchers.instanceOf(GlobalIdValueConverter.class));
  }

}
