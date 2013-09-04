package com.iotake.solr.client.gis.itest.transfer;

import org.apache.solr.common.SolrInputDocument;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.itest.transfer.AbstractTransferITest;
import com.iotake.solr.client.gis.binder.value.PositionValueConverterFactory;

public class PositionTransferITest extends AbstractTransferITest {

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinderBuilder()
        .addValueConverterFactory(Position.class,
            new PositionValueConverterFactory()).build();
  }

  private static Position createPosition(
      CoordinateReferenceSystem coordinateReferenceSystem,
      double... coordinates) {
    PositionFactoryImpl positionFactory = new PositionFactoryImpl(
        coordinateReferenceSystem);
    GeometryFactoryImpl geometryFactory = new GeometryFactoryImpl(
        positionFactory.getCoordinateReferenceSystem(), positionFactory);
    Position position = geometryFactory.createPosition(coordinates);
    return position;
  }

  @SolrDocument
  private static class WithPositon {
    @SolrId
    long id = 123;
    Position position = createPosition(DefaultGeographicCRS.WGS84, 53.15, 42.6);

  }

  @Test
  public void withPositon() {
    WithPositon bean = new WithPositon();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithPositon.class, bean.id);
    checkClass(document, WithPositon.class);
    checkClasses(document, WithPositon.class, Object.class);
    checkProperty(document, "WithPositon__id", bean.id);
    double[] coordinate = bean.position.getDirectPosition().getCoordinate();
    checkProperty(document, "WithPositon__position", coordinate[0] + " "
        + coordinate[1]);
  }

  @Test
  public void withNullPositon() {
    WithPositon bean = new WithPositon();
    bean.position = null;
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithPositon.class, bean.id);
    checkClass(document, WithPositon.class);
    checkClasses(document, WithPositon.class, Object.class);
    checkProperty(document, "WithPositon__id", bean.id);
    checkProperty(document, "WithPositon__position", (Object) null);
  }

}
