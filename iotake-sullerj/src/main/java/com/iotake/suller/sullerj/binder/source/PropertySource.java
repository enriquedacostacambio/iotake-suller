package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;

public abstract class PropertySource extends FieldSource {

  public PropertySource(Class<?> documentClass, Field field, boolean readable,
      boolean writable) {
    super(documentClass, field, readable, writable);
  }

  public SourceType getType() {
    return SourceType.PROPERTY;
  }

  public DocumentSource[] getInnerSources() {
    return new DocumentSource[0];
  }
  
  public abstract Slot[] getSlots();

}