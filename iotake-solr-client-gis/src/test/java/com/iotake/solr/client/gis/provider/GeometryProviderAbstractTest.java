package com.iotake.solr.client.gis.provider;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;

public abstract class GeometryProviderAbstractTest {

  protected abstract GeometryProvider createGeometryProvider();

  protected void toCoordinates(Position position, double longitude,
      double latitude) {
    GeometryProvider provider = createGeometryProvider();

    try {
      double[] actual = provider.toCoordinates(position);

      assertArrayEquals(new double[] { longitude, latitude }, actual, 0);
    } finally {
      if (position != null) {
        verify(position);
      }
      if (position != null) {
        verify(position);
      }
    }
  }

  @Test
  public void toCoordinates() {
    Position position = createPosition(12, 2.52);
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = NullPointerException.class)
  public void toCoordinatesWithNullPosition() {
    Position position = null;
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithTooFewCoordinates() {
    Position position = createPosition(12);
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithTooManyCoordinates() {
    Position position = createPosition(12, 2.52, 6.1);
    toCoordinates(position, 12, 2.52);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toCoordinatesWithNullCoordinates() {
    Position position = createPosition(null);
    toCoordinates(position, 12, 2.52);
  }

  protected abstract Position createPosition(double... coordinates);

  protected void toPositon(double longitude, double latitude) {
    GeometryProvider provider = createGeometryProvider();

    Position position = provider.toPositon(longitude, latitude);
    assertNotNull(position);
    DirectPosition directPosition = position.getDirectPosition();
    assertNotNull(directPosition);

    double[] coordinates = directPosition.getCoordinate();

    assertNotNull(coordinates);
    assertArrayEquals(new double[] { longitude, latitude }, coordinates, 0);
  }

  @Test
  public void toPositon() {
    toPositon(12, 2.52);
  }

}
