package com.iotake.suller.sullerj.binder.source;

import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;

public class NotMultiValuedEmbeddableClassSource extends
    NotMultiValuedClassSource {

  public NotMultiValuedEmbeddableClassSource(Class<?> documentClass,
      BeanInstantiator instantiator, FieldSource[] fieldSources) {
    super(documentClass, instantiator, fieldSources);
  }

  public SourceType getType() {
    return SourceType.EMBEDDED_CLASS;
  }

}