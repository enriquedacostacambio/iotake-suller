package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.GlobalIdValueConverterFactory;
import com.iotake.suller.sullerj.binder.value.ValueConverter;
import com.iotake.suller.sullerj.binder.value.GlobalIdValueConverterFactory.GlobalIdValueConverter;

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
