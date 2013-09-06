package com.iotake.suller.sullerj.binder.source;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.beans.BindingException;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.FieldSource;
import com.iotake.suller.sullerj.binder.util.TestDocument;

public abstract class FieldSourceAbstractTest extends
    AbstractDocumentSourceAbstractTest {

  protected abstract FieldSource createInstance(Class<?> documentClass,
      Field field, boolean readable, boolean writable);

  @Test(expected = NullPointerException.class)
  public void constructWithNullDocumentClass() {
    createInstance(null, TestDocument.DOUBLE_FIELD, false, true);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullField() {
    createInstance(TestDocument.class, null, false, true);
  }

  @Test(expected = BindingException.class)
  public void constructWithPrimitiveAnNonReadable() {
    createInstance(TestDocument.class, TestDocument.DOUBLE_FIELD, false, true);
  }

}
