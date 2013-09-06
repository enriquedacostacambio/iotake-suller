package com.iotake.suller.sullerj.binder.source;

import org.apache.solr.common.SolrInputDocument;

public interface MultiValuableDocumentSource extends DocumentSource {

  void transferEmpty(SolrInputDocument document);

}
