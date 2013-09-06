package com.iotake.suller.sullerj.gis.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.geometry.coordinate.Position;

import com.iotake.suller.sullerj.binder.util.TestDocument;
import com.iotake.suller.sullerj.binder.value.ValueConverter;
import com.iotake.suller.sullerj.binder.value.ValueConverterFactoryAbstractTest;
import com.iotake.suller.sullerj.gis.binder.value.PositionValueConverterFactory;
import com.iotake.suller.sullerj.gis.binder.value.PositionValueConverterFactory.PositionValueConverter;

public class PositionValueConverterFactoryTest extends
    ValueConverterFactoryAbstractTest {

  @Override
  protected PositionValueConverterFactory createInstance() {
    return new PositionValueConverterFactory();
  }

  @Test
  public void doCreate() {
    PositionValueConverterFactory factory = createInstance();
    ValueConverter converter = factory.doCreate(Position.class,
        TestDocument.class, TestDocument.ID_FIELD, "path__to__field", "__");
    assertThat(converter, CoreMatchers.instanceOf(PositionValueConverter.class));
  }
}
