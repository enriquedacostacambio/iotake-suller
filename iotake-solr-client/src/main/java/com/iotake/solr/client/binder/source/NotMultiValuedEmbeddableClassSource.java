package com.iotake.solr.client.binder.source;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;

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