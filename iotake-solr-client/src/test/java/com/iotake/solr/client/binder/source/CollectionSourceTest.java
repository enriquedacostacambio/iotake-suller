package com.iotake.solr.client.binder.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.easymock.EasyMock;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.collection.CollectionCreator;
import com.iotake.solr.client.binder.source.DocumentSource.SourceType;
import com.iotake.solr.client.binder.util.TestDocument;

public class CollectionSourceTest extends MultiValuedSourceAbstractTest {

  private CollectionCreator createCollectionCreator(boolean replay) {
    CollectionCreator collectionCreator = createMock(CollectionCreator.class);
    if (replay) {
      replay(collectionCreator);
    }
    return collectionCreator;
  }

  @Override
  protected CollectionSource createInstance(Class<?> documentClass) {
    return createInstance(documentClass, TestDocument.LIST_OF_STRING_FIELD,
        true, true);
  }

  @Override
  protected CollectionSource createInstance(Class<?> documentClass,
      Field field, boolean readable, boolean writable) {
    return createInstance(documentClass, field, List.class, String.class,
        createCollectionCreator(true), createItemSource(true), readable,
        writable);
  }

  private CollectionSource createInstance(Class<?> documentClass, Field field,
      Class<?> collectionType, Class<?> componentType,
      CollectionCreator collectionCreator,
      MultiValuableDocumentSource itemSource, boolean readable, boolean writable) {
    return new CollectionSource(documentClass, field, collectionType,
        componentType, collectionCreator, itemSource, readable, writable);
  }

  @Override
  protected SourceType getExpectedType() {
    return SourceType.COLLECTION;
  }

  @Test(expected = BindingException.class)
  public void constructWithNonCollection() {
    createInstance(TestDocument.class, TestDocument.STRING_FIELD, String.class,
        String.class, createCollectionCreator(true), createItemSource(true),
        false, true);
  }

  @Test(expected = BindingException.class)
  public void constructWithCollectionType() {
    createInstance(TestDocument.class, TestDocument.LIST_OF_STRING_FIELD,
        String.class, String.class, createCollectionCreator(true),
        createItemSource(true), false, true);
  }

  @Test
  public void getCollectionType() {
    CollectionSource source = createInstance(TestDocument.class,
        TestDocument.LIST_OF_STRING_FIELD, List.class, String.class,
        createCollectionCreator(true), createItemSource(true), false, true);
    assertEquals(List.class, source.getCollectionType());
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void extract(List<String> elements, Object documentElements) {
    SolrDocument document = EasyMock.createMock(SolrDocument.class);
    EasyMock.replay(document);

    CollectionCreator collectionCreator = createCollectionCreator(false);
    expect(collectionCreator.create(elements.size())).andReturn(
        new ArrayList(2));
    replay(collectionCreator);

    MultiValuableDocumentSource itemSource = createItemSource(false);
    expect(itemSource.extract(document)).andReturn(documentElements);
    replay(itemSource);

    CollectionSource source = createInstance(TestDocument.class,
        TestDocument.LIST_OF_STRING_FIELD, List.class, String.class,
        collectionCreator, itemSource, true, true);
    Object value = source.extract(document);
    assertNotNull(value);
    assertNotNull(value);
    assertThat(value, CoreMatchers.instanceOf(List.class));
    assertEquals(elements, value);

    EasyMock.verify(itemSource);
    EasyMock.verify(document);
  }

  @Test
  public void extract() {
    String element1 = "abc";
    String element2 = "def";

    List<String> elements = new LinkedList<String>();
    elements.add(element1);
    elements.add(element2);

    Collection<Object> documentElements = new LinkedList<Object>();
    documentElements.add(element1);
    documentElements.add(element2);

    extract(elements, documentElements);
  }

  @Test
  public void extractEmpty() {
    List<String> elements = Collections.emptyList();
    Collection<Object> documentElements = Collections.emptyList();

    extract(elements, documentElements);
  }

  @Test
  public void extractNull() {
    List<String> elements = Collections.emptyList();
    Collection<Object> documentElements = null;

    extract(elements, documentElements);
  }

  @Test
  public void extractSingle() {
    String element = "abc";

    List<String> elements = Collections.singletonList(element);
    Collection<Object> documentElements = Collections
        .<Object> singletonList(element);

    extract(elements, documentElements);
  }

  @Test
  public void extractNonCollection() {
    String element = "abc";

    List<String> elements = Collections.singletonList(element);
    String documentElements = element;

    extract(elements, documentElements);
  }

  private void transfer(Collection<Object> documentElements,
      List<String> elements) {
    SolrInputDocument document = EasyMock.createMock(SolrInputDocument.class);
    EasyMock.replay(document);

    CollectionCreator collectionCreator = createCollectionCreator(false);
    replay(collectionCreator);

    MultiValuableDocumentSource itemSource = createItemSource(false);
    if (documentElements.isEmpty()) {
      itemSource.transferEmpty(document);
    }
    for (Object documentElement : documentElements) {
      itemSource.transfer(documentElement, document);
    }
    replay(itemSource);

    CollectionSource source = createInstance(TestDocument.class,
        TestDocument.LIST_OF_STRING_FIELD, List.class, String.class,
        collectionCreator, itemSource, true, true);
    source.transfer(elements, document);

    EasyMock.verify(itemSource);
    EasyMock.verify(document);
  }

  @Test
  public void transfer() {
    String element1 = "abc";
    String element2 = "def";

    Collection<Object> documentElements = new LinkedList<Object>();
    documentElements.add(element1);
    documentElements.add(element2);

    List<String> elements = new LinkedList<String>();
    elements.add(element1);
    elements.add(element2);

    transfer(documentElements, elements);
  }

  @Test
  public void transferEmpty() {
    Collection<Object> documentElements = Collections.emptyList();

    List<String> elements = Collections.emptyList();

    transfer(documentElements, elements);
  }

  @Test
  public void transferSingle() {
    String element = "abc";

    Collection<Object> documentElements = Collections
        .<Object> singletonList(element);

    List<String> elements = Collections.singletonList(element);

    transfer(documentElements, elements);
  }

  @Test
  public void transferNull() {
    Collection<Object> documentElements = Collections.emptyList();

    List<String> elements = null;

    transfer(documentElements, elements);
  }

}
