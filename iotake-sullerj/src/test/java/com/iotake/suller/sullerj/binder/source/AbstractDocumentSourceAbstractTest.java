package com.iotake.suller.sullerj.binder.source;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.AbstractDocumentSource;
import com.iotake.suller.sullerj.binder.util.TestDocument;

public abstract class AbstractDocumentSourceAbstractTest extends
    DocumentSourceAbstractTest {

  @Override
  protected AbstractDocumentSource createInstance() {
    return createInstance(TestDocument.class);
  }

  protected abstract AbstractDocumentSource createInstance(
      Class<?> documentClass);

  @Test(expected = NullPointerException.class)
  public void constructWithNullDocumentClass() {
    createInstance(null);
  }

}
