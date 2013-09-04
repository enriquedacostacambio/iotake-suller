package com.iotake.solr.client.gis.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.itest.extract.AbstractExtractITest;
import com.iotake.solr.client.gis.binder.value.PositionValueConverterFactory;

public class PositionExtractITest extends AbstractExtractITest {

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinderBuilder()
        .addValueConverterFactory(Position.class,
            new PositionValueConverterFactory()).build();
  }

  @SolrDocument
  private static class WithPositon {
    @SolrId
    long id;
    Position position;
  }

  @Test
  public void withPosition() {
    long id = 123;
    double longitude = 53.15;
    double latitude = 42.6;
    EasyDocument document = new EasyDocument(WithPositon.class, id,
        WithPositon.class, WithPositon.class, Object.class).set(
        "WithPositon__id", id).set("WithPositon__position",
        longitude + " " + latitude);
    WithPositon bean = binder.getBean(document);
    assertEquals(id, bean.id);
    assertNotNull(bean.position);
    DirectPosition directPosition = bean.position.getDirectPosition();
    assertNotNull(directPosition);
    double[] coordinate = directPosition.getCoordinate();
    assertNotNull(coordinate);
    assertEquals(2, coordinate.length);
    assertEquals(longitude, coordinate[0], Double.MIN_VALUE);
    assertEquals(latitude, coordinate[1], Double.MIN_VALUE);
  }

  @Test
  public void withNullPosition() {
    long id = 123;
    EasyDocument document = new EasyDocument(WithPositon.class, id,
        WithPositon.class, WithPositon.class, Object.class).set(
        "WithPositon__id", id).set("WithPositon__position", null);
    WithPositon bean = binder.getBean(document);
    assertEquals(id, bean.id);
    assertNull(bean.position);
  }
}
