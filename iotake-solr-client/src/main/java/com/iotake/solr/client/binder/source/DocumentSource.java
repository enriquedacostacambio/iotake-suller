package com.iotake.solr.client.binder.source;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;


public interface DocumentSource {

  public static enum SourceType {
    ROOT_CLASS,

    EMBEDDED_CLASS,

    ID,

    PROPERTY,

    ENUMERATED,

    EMBEDDED,

    COLLECTION,

    ARRAY;
  }

  DocumentSource.SourceType getType();

  void transfer(Object value, SolrInputDocument document);

  Object extract(SolrDocument document);

  DocumentSource[] getInnerSources();

}