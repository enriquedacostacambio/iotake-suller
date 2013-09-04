package com.iotake.solr.client.binder.source;

import org.apache.solr.common.SolrDocument;

public abstract class NotMultiValuedClassSource extends ClassSource {

  public NotMultiValuedClassSource(Class<?> documentClass,
      FieldSource[] fieldSources) {
    super(documentClass, fieldSources);
  }

  public Object doExtract(SolrDocument document) {
    Object bean = newInstance();
    for (FieldSource fieldSource : getFieldSources()) {
      Object fieldValue = fieldSource.extract(document);
      fieldSource.setFieldValue(bean, fieldValue);
    }
    return bean;
  }

}