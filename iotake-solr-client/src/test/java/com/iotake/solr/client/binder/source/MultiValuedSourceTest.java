package com.iotake.solr.client.binder.source;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.solr.client.binder.source.DocumentSource.SourceType;
import com.iotake.solr.client.binder.util.TestDocument;

public class MultiValuedSourceTest extends MultiValuedSourceAbstractTest {

  @Override
  protected MultiValuedSource createInstance(Class<?> documentClass,
      Field field, boolean readable, boolean writable) {
    DocumentSource itemSource = createItemSource(true);
    return new MultiValuedSource(documentClass, field, String.class,
        itemSource, readable, writable) {

      public SourceType getType() {
        return getExpectedType();
      }

      @Override
      protected void doFieldTransfer(Object value, SolrInputDocument document) {
        throw new UnsupportedOperationException();
      }

      @Override
      protected Object doFieldExtract(SolrDocument document) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  protected MultiValuedSource createInstance(Class<?> documentClass) {
    return createInstance(TestDocument.class,
        TestDocument.LIST_OF_STRING_FIELD, true, true);
  }

  @Override
  protected SourceType getExpectedType() {
    return SourceType.COLLECTION;
  }

  @Test
  public void getComponentType() {
    MultiValuedSource source = createInstance(TestDocument.class,
        TestDocument.LIST_OF_STRING_FIELD, true, true);
    assertEquals(String.class, source.getComponentType());
  }

  @Test
  public void getItemSource() {
    MultiValuableDocumentSource itemSource = createItemSource(true);
    MultiValuedSource source = createInstance(itemSource);
    assertEquals(itemSource, source.getItemSource());
  }

}
