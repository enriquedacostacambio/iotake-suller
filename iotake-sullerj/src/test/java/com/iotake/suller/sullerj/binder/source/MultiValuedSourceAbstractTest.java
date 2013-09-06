package com.iotake.suller.sullerj.binder.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;

import java.lang.reflect.Field;

import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.ArraySource;
import com.iotake.suller.sullerj.binder.source.MultiValuableDocumentSource;
import com.iotake.suller.sullerj.binder.source.MultiValuedSource;
import com.iotake.suller.sullerj.binder.util.TestDocument;

public abstract class MultiValuedSourceAbstractTest extends
    FieldSourceAbstractTest {

  protected MultiValuableDocumentSource createItemSource(boolean replay) {
    MultiValuableDocumentSource itemSource = createMock(MultiValuableDocumentSource.class);
    if (replay) {
      replay(itemSource);
    }
    return itemSource;
  }

  protected MultiValuedSource createInstance(MultiValuableDocumentSource itemSource) {
    return createInstance(TestDocument.class, TestDocument.INT_ARRAY_FIELD,
        int.class, itemSource, true, true);
  }

  private MultiValuedSource createInstance(Class<?> documentClass, Field field,
      Class<?> componentType, MultiValuableDocumentSource itemSource, boolean readable,
      boolean writable) {
    return new ArraySource(documentClass, field, componentType, itemSource,
        readable, writable);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullComponentType() {
    createInstance(TestDocument.class, null, null, createItemSource(true),
        false, true);
  }

  @Test(expected = NullPointerException.class)
  public void constructWithNullItemSource() {
    createInstance(TestDocument.class, null, int.class, null, false, true);
  }


}
