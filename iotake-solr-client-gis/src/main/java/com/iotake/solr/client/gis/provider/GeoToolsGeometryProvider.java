package com.iotake.solr.client.gis.provider;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoToolsGeometryProvider implements GeometryProvider {

  private final PositionFactoryImpl positionFactory;

  private final GeometryFactoryImpl geometryFactory;

  public GeoToolsGeometryProvider() {
    this(DefaultGeographicCRS.WGS84);
  }

  public GeoToolsGeometryProvider(
      CoordinateReferenceSystem coordinateReferenceSystem) {
    checkNotNull(coordinateReferenceSystem,
        "Coordinate reference system cannot be null.");
    positionFactory = new PositionFactoryImpl(coordinateReferenceSystem);
    geometryFactory = new GeometryFactoryImpl(
        positionFactory.getCoordinateReferenceSystem(), positionFactory);
  }

  public Position toPositon(double longitude, double latitude) {
    Position position = geometryFactory.createPosition(new double[] {
        longitude, latitude });
    return position;
  }

  public double[] toCoordinates(Position position) {
    checkNotNull(position, "Position cannot be null.");
    DirectPosition directPosition = position.getDirectPosition();
    checkArgument(directPosition != null, "Direct position cannot be null.");
    CoordinateReferenceSystem coordinateReferenceSystem = directPosition
        .getCoordinateReferenceSystem();
    checkArgument(coordinateReferenceSystem != null,
        "Coordinate reference system cannot be null.");
    checkArgument(coordinateReferenceSystem.equals(positionFactory
        .getCoordinateReferenceSystem()),
        "Invalid coordinate reference system: " + coordinateReferenceSystem);
    double[] coordinates = directPosition.getCoordinate();
    checkArgument(coordinates != null, "Coordinates cannot be null.");
    checkArgument(coordinates.length == 2,
        "Invalid number of Position coordinates: " + coordinates.length);
    return coordinates;
  }

}
