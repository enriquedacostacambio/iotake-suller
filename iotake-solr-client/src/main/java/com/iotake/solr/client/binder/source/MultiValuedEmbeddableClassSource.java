package com.iotake.solr.client.binder.source;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;

public class MultiValuedEmbeddableClassSource extends MultiValuedClassSource {

  public MultiValuedEmbeddableClassSource(Class<?> documentClass,
      BeanInstantiator instantiator, FieldSource[] fieldSources, String path) {
    super(documentClass, instantiator, fieldSources, path);
  }

  public SourceType getType() {
    return SourceType.EMBEDDED_CLASS;
  }

}