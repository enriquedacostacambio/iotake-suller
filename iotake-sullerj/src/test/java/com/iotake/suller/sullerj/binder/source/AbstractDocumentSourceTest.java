package com.iotake.suller.sullerj.binder.source;

import static org.junit.Assert.assertEquals;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.AbstractDocumentSource;
import com.iotake.suller.sullerj.binder.source.DocumentSource;
import com.iotake.suller.sullerj.binder.source.DocumentSource.SourceType;
import com.iotake.suller.sullerj.binder.util.TestDocument;

public class AbstractDocumentSourceTest extends
    AbstractDocumentSourceAbstractTest {

  protected AbstractDocumentSource createInstance(Class<?> documentClass) {
    return new AbstractDocumentSource(documentClass) {
      public SourceType getType() {
        return getExpectedType();
      }

      public DocumentSource[] getInnerSources() {
        return new DocumentSource[0];
      }

      @Override
      protected Object doExtract(SolrDocument document) {
        throw new UnsupportedOperationException();
      }

      @Override
      protected void doTransfer(Object value, SolrInputDocument document) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Override
  protected SourceType getExpectedType() {
    return SourceType.PROPERTY;
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullDocumentClass() {
    createInstance(null);
  }

  @Test
  public void getInnerSources() {
    AbstractDocumentSource source = createInstance(TestDocument.class);
    assertEquals(TestDocument.class, source.getDocumentClass());
  }

}
