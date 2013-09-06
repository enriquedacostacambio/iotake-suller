package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;

import org.apache.solr.common.SolrInputDocument;

public abstract class MultiValuedSource extends FieldSource implements
    MultiValuableDocumentSource {

  private final DocumentSource itemSource;

  private final Class<?> componentType;

  public MultiValuedSource(Class<?> documentClass, Field field,
      Class<?> componentType, DocumentSource itemSource, boolean readable,
      boolean writable) {
    super(documentClass, field, readable, writable);
    this.itemSource = itemSource;
    this.componentType = componentType;
  }

  public DocumentSource[] getInnerSources() {
    return new DocumentSource[] { itemSource };
  }

  public Class<?> getComponentType() {
    return componentType;
  }

  protected DocumentSource getItemSource() {
    return itemSource;
  }

  public void transferEmpty(SolrInputDocument document) {
    ((MultiValuableDocumentSource) itemSource).transferEmpty(document);
  }
}