package com.iotake.solr.client.binder.source;

public class MultiValuedEmbeddableClassSource extends MultiValuedClassSource {

  public MultiValuedEmbeddableClassSource(Class<?> documentClass,
      FieldSource[] fieldSources, String path) {
    super(documentClass, fieldSources, path);
  }

  public SourceType getType() {
    return SourceType.EMBEDDED_CLASS;
  }

}