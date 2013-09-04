package com.iotake.solr.client.binder.postprocessor;

import org.apache.solr.common.SolrInputDocument;

public interface DocumentPostProcessor {

  SolrInputDocument postProcess(Object bean, SolrInputDocument document);

}
