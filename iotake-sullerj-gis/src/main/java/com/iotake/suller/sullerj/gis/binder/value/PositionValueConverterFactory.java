package com.iotake.suller.sullerj.gis.binder.value;

import java.lang.reflect.Field;

import org.opengis.geometry.coordinate.Position;

import com.iotake.suller.sullerj.binder.value.AbstractSingleSlotValueConverter;
import com.iotake.suller.sullerj.binder.value.AbstractValueConverterFactory;
import com.iotake.suller.sullerj.gis.provider.GeoToolsGeometryProvider;
import com.iotake.suller.sullerj.gis.provider.GeometryProvider;

public class PositionValueConverterFactory extends
    AbstractValueConverterFactory {

  public static class PositionValueConverter extends
      AbstractSingleSlotValueConverter {

    private final GeometryProvider geometryProvider;

    public PositionValueConverter(Class<?> targetType, Class<?> documentClass,
        Field field, GeometryProvider geometryProvider, String path) {
      super(Position.class, targetType, documentClass, field, path);
      this.geometryProvider = geometryProvider;
    }

    public Class<?> getSlotType() {
      return String.class;
    }

    public Object toDocumentValue(Object beanValue) {
      if (beanValue == null) {
        return null;
      }
      checkBeanValueType(beanValue);
      Position position = (Position) beanValue;
      double[] coordinates = geometryProvider.toCoordinates(position);
      return coordinates[0] + " " + coordinates[1];
    }

    public Object toBeanValue(Object documentValue) {
      if (documentValue == null) {
        return null;
      }
      checkDocumentValueType(documentValue, getSlot());
      String string = (String) documentValue;
      int indexOfSpace = string.indexOf(' ');
      if (indexOfSpace == -1 || indexOfSpace == 0
          || indexOfSpace == string.length() - 1) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Expecting longitude and longitude separated by a single space.");
      }
      double longitude;
      double latitude;
      try {
        longitude = Double.parseDouble(string.substring(0, indexOfSpace));
        latitude = Double.parseDouble(string.substring(indexOfSpace + 1));
      } catch (NumberFormatException e) {
        throw reportInvalidDocumentValue(documentValue, getSlot(),
            "Invalid longitude-longitude numbers.", e);
      }
      Position position = geometryProvider.toPositon(longitude, latitude);
      return position;
    }
  }

  private final GeometryProvider geometryProvider;

  public PositionValueConverterFactory() {
    this(new GeoToolsGeometryProvider());
  }

  public PositionValueConverterFactory(GeometryProvider geometryProvider) {
    super(Position.class);
    this.geometryProvider = geometryProvider;
  }

  public PositionValueConverter doCreate(Class<?> targetType,
      Class<?> documentClass, Field field, String slotName,
      String slotNameSeparator) {
    return new PositionValueConverter(targetType, documentClass, field,
        geometryProvider, slotName);
  }
}