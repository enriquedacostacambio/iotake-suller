package com.iotake.suller.sullerj.gis;

import java.lang.reflect.Field;

import org.opengis.geometry.coordinate.Position;

import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.util.TestDocument;

@SolrDocument
public class GisTestDocument extends TestDocument {

  public static final Field POSITION_FIELD = getField("positionField");

  private Position positionField;

  public Position getPositionField() {
    return positionField;
  }

  public void setPositionField(Position positionField) {
    this.positionField = positionField;
  }

  private static Field getField(String fieldName) {
    return getField(GisTestDocument.class, fieldName);
  }

}