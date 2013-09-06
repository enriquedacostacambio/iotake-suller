package com.iotake.suller.sullerj.binder.source;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.easymock.EasyMock;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.source.ArraySource;
import com.iotake.suller.sullerj.binder.source.MultiValuableDocumentSource;
import com.iotake.suller.sullerj.binder.source.DocumentSource.SourceType;
import com.iotake.suller.sullerj.binder.util.TestDocument;

public class ArraySourceTest extends MultiValuedSourceAbstractTest {

  @Override
  protected ArraySource createInstance(Class<?> documentClass) {
    return createInstance(documentClass, TestDocument.INT_ARRAY_FIELD,
        int.class, createItemSource(true), true, true);
  }

  @Override
  protected ArraySource createInstance(Class<?> documentClass, Field field,
      boolean readable, boolean writable) {
    return createInstance(documentClass, field, int.class,
        createItemSource(true), readable, writable);
  }

  private ArraySource createInstance(Class<?> documentClass, Field field,
      Class<?> componentType, MultiValuableDocumentSource itemSource,
      boolean readable, boolean writable) {
    return new ArraySource(documentClass, field, componentType, itemSource,
        readable, writable);
  }

  @Override
  protected SourceType getExpectedType() {
    return SourceType.ARRAY;
  }

  @Test(expected = BindingException.class)
  public void constructWithNonArray() {
    createInstance(TestDocument.class, TestDocument.STRING_FIELD, String.class,
        createItemSource(true), false, true);
  }

  private void extract(int[] elements, Object documentElements) {
    SolrDocument document = EasyMock.createMock(SolrDocument.class);
    EasyMock.replay(document);

    MultiValuableDocumentSource itemSource = createItemSource(false);
    expect(itemSource.extract(document)).andReturn(documentElements);
    replay(itemSource);

    ArraySource source = createInstance(TestDocument.class,
        TestDocument.INT_ARRAY_FIELD, int.class, itemSource, true, true);
    Object value = source.extract(document);
    assertNotNull(value);
    assertNotNull(value);
    assertThat(value, CoreMatchers.instanceOf(int[].class));
    assertArrayEquals(elements, (int[]) value);

    EasyMock.verify(itemSource);
    EasyMock.verify(document);
  }

  @Test
  public void extract() {
    int element1 = 123;
    int element2 = 456;

    int[] elements = { element1, element2 };
    Collection<Object> documentElements = new LinkedList<Object>();
    documentElements.add(element1);
    documentElements.add(element2);

    extract(elements, documentElements);
  }

  @Test
  public void extractEmpty() {
    int[] elements = {};
    Collection<Object> documentElements = Collections.emptyList();

    extract(elements, documentElements);
  }

  @Test
  public void extractNull() {
    int[] elements = {};
    Collection<Object> documentElements = null;

    extract(elements, documentElements);
  }

  @Test
  public void extractSingle() {
    int element = 123;

    int[] elements = { element };
    Collection<Object> documentElements = Collections
        .<Object> singletonList(element);

    extract(elements, documentElements);
  }

  @Test
  public void extractNonCollection() {
    int element = 123;

    int[] elements = { element };
    Integer documentElements = element;

    extract(elements, documentElements);
  }

  private void transfer(Collection<Object> documentElements, int[] elements) {
    SolrInputDocument document = EasyMock.createMock(SolrInputDocument.class);
    EasyMock.replay(document);

    MultiValuableDocumentSource itemSource = createItemSource(false);
    if (documentElements.isEmpty()) {
      itemSource.transferEmpty(document);
    }
    for (Object documentElement : documentElements) {
      itemSource.transfer(documentElement, document);
    }
    replay(itemSource);

    ArraySource source = createInstance(TestDocument.class,
        TestDocument.INT_ARRAY_FIELD, int.class, itemSource, true, true);
    source.transfer(elements, document);

    EasyMock.verify(itemSource);
    EasyMock.verify(document);
  }

  @Test
  public void transfer() {
    int element1 = 123;
    int element2 = 456;

    Collection<Object> documentElements = new LinkedList<Object>();
    documentElements.add(element1);
    documentElements.add(element2);

    int[] elements = { element1, element2 };

    transfer(documentElements, elements);
  }

  @Test
  public void transferEmpty() {
    Collection<Object> documentElements = Collections.emptyList();

    int[] elements = {};

    transfer(documentElements, elements);
  }

  @Test
  public void transferSingle() {
    int element = 123;

    Collection<Object> documentElements = Collections
        .<Object> singletonList(element);

    int[] elements = { element };

    transfer(documentElements, elements);
  }

  @Test
  public void transferNull() {
    Collection<Object> documentElements = Collections.emptyList();

    int[] elements = null;

    transfer(documentElements, elements);
  }

}
