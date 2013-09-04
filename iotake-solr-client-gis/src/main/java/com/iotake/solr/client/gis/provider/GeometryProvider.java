package com.iotake.solr.client.gis.provider;

import org.opengis.geometry.coordinate.Position;

public interface GeometryProvider {

  double[] toCoordinates(Position position);

  Position toPositon(double longitude, double latitude);

}
