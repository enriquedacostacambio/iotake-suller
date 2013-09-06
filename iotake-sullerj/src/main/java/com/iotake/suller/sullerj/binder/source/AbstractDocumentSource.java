package com.iotake.suller.sullerj.binder.source;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public abstract class AbstractDocumentSource implements DocumentSource {

  private final Class<?> documentClass;

  public AbstractDocumentSource(Class<?> documentClass) {
    checkNotNull(documentClass, "Document class cannot be null.");
    this.documentClass = documentClass;
  }

  public Class<?> getDocumentClass() {
    return documentClass;
  }

  public Object extract(SolrDocument document) {
    checkNotNull(document, "Document cannot be null");
    return doExtract(document);
  }

  public void transfer(Object value, SolrInputDocument document) {
    checkNotNull(document, "Document cannot be null");
    doTransfer(value, document);
  }

  protected abstract Object doExtract(SolrDocument document);

  protected abstract void doTransfer(Object value, SolrInputDocument document);

}