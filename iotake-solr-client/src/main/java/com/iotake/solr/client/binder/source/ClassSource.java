package com.iotake.solr.client.binder.source;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrInputDocument;

import com.iotake.solr.client.binder.instantiator.BeanInstantiator;

public abstract class ClassSource extends AbstractDocumentSource {

  private final BeanInstantiator instantiator;
  private final FieldSource[] fieldSources;

  public ClassSource(Class<?> documentClass, BeanInstantiator instantiator,
      FieldSource[] fieldSources) {
    super(documentClass);
    this.instantiator = instantiator;
    this.fieldSources = fieldSources;
  }

  protected FieldSource[] getFieldSources() {
    return fieldSources;
  }

  public Object instantiate() {
    try {
      return instantiator.instantiate();
    } catch (Exception e) {
      throw new BindingException("Error creating document "
          + getDocumentClass().getName() + ": " + e, e);
    }
  }

  public DocumentSource[] getInnerSources() {
    return fieldSources.clone();
  }

  public void doTransfer(Object bean, SolrInputDocument document) {
    for (FieldSource fieldSource : getFieldSources()) {
      Object fieldValue = fieldSource.getFieldValue(bean);
      fieldSource.transfer(fieldValue, document);
    }
  }

}