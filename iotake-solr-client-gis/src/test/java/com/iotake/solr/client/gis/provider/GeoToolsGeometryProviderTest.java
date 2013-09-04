package com.iotake.solr.client.gis.provider;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoToolsGeometryProviderTest extends GeometryProviderAbstractTest {

  @Override
  protected GeoToolsGeometryProvider createGeometryProvider() {
    return new GeoToolsGeometryProvider();
  }

  @Test
  public void createWithCoordinateReferenceSystem() {
    new GeoToolsGeometryProvider(DefaultGeographicCRS.WGS84);
  }

  @Test(expected = NullPointerException.class)
  public void createWithNullCoordinateReferenceSystem() {
    new GeoToolsGeometryProvider(null);
  }

  @Override
  protected Position createPosition(double... coordinates) {
    return createPosition(false, DefaultGeographicCRS.WGS84, coordinates);
  }

  private Position createPosition(boolean nullDirectPosition,
      CoordinateReferenceSystem coordinateReferenceSystem,
      double... coordinates) {
    DirectPosition directPosition = null;
    if (!nullDirectPosition) {
      directPosition = createMock(DirectPosition.class);
      expect(directPosition.getCoordinateReferenceSystem()).andReturn(
          coordinateReferenceSystem);
      expect(directPosition.getCoordinate()).andReturn(coordinates);
      replay(directPosition);
    }

    Position position = createMock(Position.class);
    expect(position.getDirectPosition()).andReturn(directPosition);
    replay(position);
    return position;
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithNullDirectPosition() {
    Position position = createPosition(true, DefaultGeographicCRS.WGS84, 12,
        2.52);
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithNullCoordinateReferenceSystem() {
    Position position = createPosition(false, null, 12, 2.52);
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithNullMismatchingCoordinateReferenceSystem() {
    Position position = createPosition(false, DefaultGeographicCRS.WGS84_3D,
        12, 2.52);
    toCoordinates(position, 12, 2.52);
  }
}
