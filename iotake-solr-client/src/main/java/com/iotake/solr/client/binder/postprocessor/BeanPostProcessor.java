package com.iotake.solr.client.binder.postprocessor;

import org.apache.solr.common.SolrDocument;

public interface BeanPostProcessor {
  
  Object postProcess(SolrDocument document, Object bean) throws Exception;

}
