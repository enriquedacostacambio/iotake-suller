package com.iotake.solr.client.binder.source;


public class NotMultiValuedEmbeddableClassSource extends
    NotMultiValuedClassSource {

  public NotMultiValuedEmbeddableClassSource(Class<?> documentClass,
      FieldSource[] fieldSources) {
    super(documentClass, fieldSources);
  }

  public SourceType getType() {
    return SourceType.EMBEDDED_CLASS;
  }

}