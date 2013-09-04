package com.iotake.solr.client.binder.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.iotake.solr.client.binder.source.DocumentSource.SourceType;

public abstract class DocumentSourceAbstractTest {

  protected abstract DocumentSource createInstance();

  @Test
  public void getType() {
    DocumentSource source = createInstance();
    SourceType type = source.getType();
    assertEquals(getExpectedType(), type);
  }

  protected abstract SourceType getExpectedType();

  @Test(expected = NullPointerException.class)
  public void transferWithNullDocument() {
    DocumentSource source = createInstance();
    source.transfer("aValue", null);
  }

  @Test(expected = NullPointerException.class)
  public void extractWithNullDocument() {
    DocumentSource source = createInstance();
    source.extract(null);
  }

  @Test
  public void getInnerSources() {
    DocumentSource source = createInstance();
    DocumentSource[] innerSources = source.getInnerSources();
    assertNotNull(innerSources);
    for (DocumentSource innerSource : innerSources) {
      assertNotNull(innerSource);
    }
  }

}
