package com.iotake.solr.client.binder.source;

import java.lang.reflect.Field;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class EmbeddedSource extends FieldSource implements
    MultiValuableDocumentSource {

  private final ClassSource embeddedClasSource;

  public EmbeddedSource(Class<?> documentClass, Field field, boolean readable,
      boolean writable, ClassSource embeddedClasSource) {
    super(documentClass, field, readable, writable);
    this.embeddedClasSource = embeddedClasSource;
  }

  public SourceType getType() {
    return SourceType.EMBEDDED;
  }

  public DocumentSource[] getInnerSources() {
    return new DocumentSource[] { embeddedClasSource };
  }

  public void transferEmpty(SolrInputDocument document) {
    ((MultiValuableDocumentSource) embeddedClasSource).transferEmpty(document);
  }

  protected void doFieldTransfer(Object value, SolrInputDocument document) {
    embeddedClasSource.transfer(value, document);
  }

  protected Object doFieldExtract(SolrDocument document) {
    return embeddedClasSource.extract(document);
  }

}