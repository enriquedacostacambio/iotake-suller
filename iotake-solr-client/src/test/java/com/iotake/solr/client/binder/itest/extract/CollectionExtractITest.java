package com.iotake.solr.client.binder.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrEmbeddable;
import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.annotation.SolrTarget;
import com.iotake.solr.client.binder.annotation.SolrTargetCollection;

public class CollectionExtractITest extends AbstractExtractITest {

  @SolrDocument
  private static class WithCollection {
    @SolrId
    long id;
    Set<String> collection;
  }

  @Test
  public void withCollection() {
    long id = 123;
    Set<String> collection = new HashSet<String>();
    {
      collection.add("abc");
      collection.add("def");
    }
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", collection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertEquals(collection, bean.collection);
  }

  @SolrEmbeddable
  private static class CollectionEmbeddable {
    Collection<Integer> embeddedCollection;
  }

  @SolrDocument
  private static class WithEmbeddedCollection {
    @SolrId
    long id;
    CollectionEmbeddable embedded;
  }

  @Test
  public void withEmbeddedCollection() {
    long id = 123;
    List<Integer> embeddedCollection = Arrays
        .asList(new Integer[] { 456, 789 });
    EasyDocument document = new EasyDocument(id, WithEmbeddedCollection.class,
        WithEmbeddedCollection.class, Object.class).set(
        "WithEmbeddedCollection__id", id).set(
        "WithEmbeddedCollection__embedded__embeddedCollection",
        embeddedCollection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithEmbeddedCollection.class));
    WithEmbeddedCollection bean = (WithEmbeddedCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.embedded);
    assertEquals(embeddedCollection, bean.embedded.embeddedCollection);
  }

  @SolrEmbeddable
  private static class SimpleEmbeddable {
    Short embeddedProperty;
  }

  @SolrDocument
  private static class WithCollectionOfEmbeddables {
    @SolrId
    long id;
    List<SimpleEmbeddable> collection;
  }

  @Test
  public void withCollectionOfEmbeddables() {
    long id = 123;
    List<Short> embeddedProperties = new LinkedList<Short>();
    {
      embeddedProperties.add((short) 23);
      embeddedProperties.add((short) 45);
    }
    EasyDocument document = new EasyDocument(id,
        WithCollectionOfEmbeddables.class, WithCollectionOfEmbeddables.class,
        Object.class).set("WithCollectionOfEmbeddables__id", id).set(
        "WithCollectionOfEmbeddables__collection__embeddedProperty",
        embeddedProperties);
    Object object = binder.getBean(document);
    assertThat(object,
        CoreMatchers.instanceOf(WithCollectionOfEmbeddables.class));
    WithCollectionOfEmbeddables bean = (WithCollectionOfEmbeddables) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(2, bean.collection.size());
    SimpleEmbeddable first = bean.collection.get(0);
    assertNotNull(first);
    assertEquals(embeddedProperties.get(0), first.embeddedProperty);
    SimpleEmbeddable second = bean.collection.get(1);
    assertEquals(embeddedProperties.get(1), second.embeddedProperty);
  }

  @SolrDocument
  private static class WithTargetCollection {
    @SolrId
    long id;
    @SolrTargetCollection(LinkedList.class)
    Collection<String> collection;
  }

  @Test
  public void withTargetCollection() {
    long id = 123;
    Collection<String> collection = new ArrayList<String>();
    {
      collection.add("abc");
      collection.add("def");
    }
    EasyDocument document = new EasyDocument(id, WithTargetCollection.class,
        WithTargetCollection.class, Object.class).set(
        "WithTargetCollection__id", id).set("WithTargetCollection__collection",
        collection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithTargetCollection.class));
    WithTargetCollection bean = (WithTargetCollection) object;
    assertEquals(id, bean.id);
    assertThat(bean.collection, CoreMatchers.instanceOf(LinkedList.class));
    assertEquals(collection, bean.collection);
  }

  @SolrDocument
  private static class WithTargetAndTargetCollection {
    @SolrId
    long id;
    @SolrTarget(String.class)
    @SolrTargetCollection(LinkedList.class)
    Collection<CharSequence> collection;
  }

  @Test
  public void withTargetAndTargetCollection() {
    long id = 123;
    String firstValue = "abc";
    String secondValue = "def";
    Collection<String> collection = new HashSet<String>();
    {
      collection.add(firstValue);
      collection.add(secondValue);
    }
    EasyDocument document = new EasyDocument(id,
        WithTargetAndTargetCollection.class,
        WithTargetAndTargetCollection.class, Object.class).set(
        "WithTargetAndTargetCollection__id", id).set(
        "WithTargetAndTargetCollection__collection", collection);
    Object object = binder.getBean(document);
    assertThat(object,
        CoreMatchers.instanceOf(WithTargetAndTargetCollection.class));
    WithTargetAndTargetCollection bean = (WithTargetAndTargetCollection) object;
    assertEquals(id, bean.id);
    assertThat(bean.collection, CoreMatchers.instanceOf(LinkedList.class));
    assertEquals(2, bean.collection.size());
    List<?> list = (List<?>) bean.collection;
    Object first = list.get(0);
    assertThat(first, CoreMatchers.instanceOf(String.class));
    assertEquals(firstValue, first);
    Object second = list.get(1);
    assertThat(second, CoreMatchers.instanceOf(String.class));
    assertEquals(secondValue, second);
  }

  @Test
  public void withNullCollection() {
    long id = 123;
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(0, bean.collection.size());
  }

  @Test
  public void withEmptyCollection() {
    long id = 123;
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(0, bean.collection.size());
  }

  @Test
  public void withSingleElementCollection() {
    long id = 123;
    Set<String> collection = Collections.singleton("abc");
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", collection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(collection, bean.collection);
  }

  @Test
  public void withNullElementCollection() {
    long id = 123;
    Set<String> collection = new HashSet<String>();
    {
      collection.add("abc");
      collection.add(null);
    }
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", collection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(collection, bean.collection);
  }

  @Test
  public void withNullElementCollectionAndNoNullTrick() {
    binder = new ExtendedDocumentObjectBinderBuilder().setNullTrickEnabled(
        false).build();

    long id = 123;
    Set<String> collection = new HashSet<String>();
    {
      collection.add("abc");
      collection.add(null);
    }
    EasyDocument document = new EasyDocument(id, WithCollection.class,
        WithCollection.class, Object.class).set("WithCollection__id", id).set(
        "WithCollection__collection", collection);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithCollection.class));
    WithCollection bean = (WithCollection) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.collection);
    assertEquals(collection, bean.collection);
  }
}
