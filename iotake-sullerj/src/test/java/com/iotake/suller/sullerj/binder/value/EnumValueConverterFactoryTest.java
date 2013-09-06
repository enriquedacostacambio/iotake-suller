package com.iotake.suller.sullerj.binder.value;

import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.EnumValueConverterFactory;
import com.iotake.suller.sullerj.binder.value.ValueConverter;
import com.iotake.suller.sullerj.binder.value.EnumValueConverterFactory.NameEnumValueConverter;
import com.iotake.suller.sullerj.binder.value.EnumValueConverterFactory.OrdinalEnumValueConverter;

public class EnumValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected EnumValueConverterFactory createInstance() {
    return new EnumValueConverterFactory();
  }

  @Test
  public void doCreateWithDefaultEnumType() {
    EnumValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(TimeUnit.class,
        TestDocument.class, TestDocument.DEFAULT_ENUM_FIELD, "path__to__field",
        "__");
    assertThat(converter, CoreMatchers.instanceOf(NameEnumValueConverter.class));
  }

  @Test
  public void doCreateWithNameEnumType() {
    EnumValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(TimeUnit.class,
        TestDocument.class, TestDocument.NAME_ENUM_FIELD, "path__to__field",
        "__");
    assertThat(converter, CoreMatchers.instanceOf(NameEnumValueConverter.class));
  }

  @Test
  public void doCreateWithOrdinalEnumType() {
    EnumValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(TimeUnit.class,
        TestDocument.class, TestDocument.ORDINAL_ENUM_FIELD, "path__to__field",
        "__");
    assertThat(converter,
        CoreMatchers.instanceOf(OrdinalEnumValueConverter.class));
  }

}
