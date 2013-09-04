package com.iotake.solr.client.gis.binder.value;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.geometry.coordinate.Position;

import com.iotake.solr.client.binder.util.TestDocument;
import com.iotake.solr.client.binder.value.ValueConverter;
import com.iotake.solr.client.binder.value.ValueConverterFactoryAbstractTest;
import com.iotake.solr.client.gis.binder.value.PositionValueConverterFactory.PositionValueConverter;

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
