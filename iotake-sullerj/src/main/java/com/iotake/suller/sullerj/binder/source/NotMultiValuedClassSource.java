package com.iotake.suller.sullerj.binder.source;

import org.apache.solr.common.SolrDocument;

import com.iotake.suller.sullerj.binder.instantiator.BeanInstantiator;

public abstract class NotMultiValuedClassSource extends ClassSource {

  public NotMultiValuedClassSource(Class<?> documentClass,
      BeanInstantiator instantiator, FieldSource[] fieldSources) {
    super(documentClass, instantiator, fieldSources);
  }

  public Object doExtract(SolrDocument document) {
    Object bean = instantiate();
    for (FieldSource fieldSource : getFieldSources()) {
      Object fieldValue = fieldSource.extract(document);
      fieldSource.setFieldValue(bean, fieldValue);
    }
    return bean;
  }

}