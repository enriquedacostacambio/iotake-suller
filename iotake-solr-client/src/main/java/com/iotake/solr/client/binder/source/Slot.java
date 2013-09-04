package com.iotake.solr.client.binder.source;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.primitives.Primitives;

public class Slot {

  private final String path;

  private final Class<?> type;

  private final Class<?> wrapperType;

  public Slot(String path, Class<?> type) {
    checkNotNull(path, "Path cannot be null.");
    checkNotNull(type, "Type cannot be null.");
    this.path = path;
    this.type = type;
    this.wrapperType = Primitives.wrap(type);
  }

  public String getPath() {
    return path;
  }

  public Class<?> getType() {
    return type;
  }

  @Override
  public String toString() {
    return path + ":" + type.getName();
  }

  public Class<?> getWrapperType() {
    return wrapperType;
  }
}