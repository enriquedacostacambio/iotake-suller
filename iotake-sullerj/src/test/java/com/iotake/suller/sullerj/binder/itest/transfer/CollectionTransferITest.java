package com.iotake.suller.sullerj.binder.itest.transfer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinder;
import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEmbeddable;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrTarget;
import com.iotake.suller.sullerj.binder.annotation.SolrTargetCollection;
import com.iotake.suller.sullerj.binder.source.MultiValuedPropertySource;

public  class CollectionTransferITest extends AbstractTransferITest {

  @SolrDocument
  private static class WithCollection {
    @SolrId
    long id = 123;
    Set<String> collection = new HashSet<String>();
    {
      collection.add("abc");
      collection.add("def");
    }
  }

  @Test
  public void withCollection() {
    WithCollection bean = new WithCollection();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    checkProperty(document, "WithCollection__collection", bean.collection);
  }

  @SolrEmbeddable
  private static class CollectionEmbeddable {
    List<Integer> embeddedCollection = Arrays
        .asList(new Integer[] { 456, 789 });
  }

  @SolrDocument
  private static class WithEmbeddedCollection {
    @SolrId
    long id = 123;
    CollectionEmbeddable embedded = new CollectionEmbeddable();
  }

  @Test
  public void withEmbeddedCollection() {
    WithEmbeddedCollection bean = new WithEmbeddedCollection();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithEmbeddedCollection.class, bean.id);
    checkClass(document, WithEmbeddedCollection.class);
    checkClasses(document, WithEmbeddedCollection.class, Object.class);
    checkProperty(document, "WithEmbeddedCollection__id", bean.id);
    checkProperty(document,
        "WithEmbeddedCollection__embedded__embeddedCollection",
        bean.embedded.embeddedCollection);
  }

  @SolrEmbeddable
  private static class SimpleEmbeddable {
    Short embeddedProperty = 45;

    @SuppressWarnings("unused")
    SimpleEmbeddable() {
    }
    
    SimpleEmbeddable(int embeddedProperty) {
      this.embeddedProperty = (short) embeddedProperty;
    }
  }

  @SolrDocument
  private static class WithCollectionOfEmbeddables {
    @SolrId
    long id = 123;
    List<SimpleEmbeddable> collection = new LinkedList<SimpleEmbeddable>();
    {
      collection.add(new SimpleEmbeddable(23));
      collection.add(new SimpleEmbeddable(45));
    }
  }

  @Test
  public void withCollectionOfEmbeddables() {
    WithCollectionOfEmbeddables bean = new WithCollectionOfEmbeddables();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollectionOfEmbeddables.class, bean.id);
    checkClass(document, WithCollectionOfEmbeddables.class);
    checkClasses(document, WithCollectionOfEmbeddables.class, Object.class);
    checkProperty(document, "WithCollectionOfEmbeddables__id", bean.id);
    checkProperty(
        document,
        "WithCollectionOfEmbeddables__collection__embeddedProperty",
        new Short[] { bean.collection.get(0).embeddedProperty,
            bean.collection.get(1).embeddedProperty });
  }

  @SolrDocument
  private static class WithTargetCollection {
    @SolrId
    long id = 123;
    @SolrTargetCollection(LinkedList.class)
    Collection<String> collection = new HashSet<String>();
    {
      collection.add("abc");
      collection.add("def");
    }
  }

  @Test
  public void withTargetCollection() {
    WithTargetCollection bean = new WithTargetCollection();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithTargetCollection.class, bean.id);
    checkClass(document, WithTargetCollection.class);
    checkClasses(document, WithTargetCollection.class, Object.class);
    checkProperty(document, "WithTargetCollection__id", bean.id);
    checkProperty(document, "WithTargetCollection__collection", bean.collection);
  }

  @SolrDocument
  private static class WithTargetAndTargetCollection {
    @SolrId
    long id = 123;
    @SolrTarget(String.class)
    @SolrTargetCollection(LinkedList.class)
    Collection<CharSequence> collection = new HashSet<CharSequence>();
    {
      collection.add("abc");
      collection.add("def");
    }
  }

  @Test
  public void withTargetAndTargetCollection() {
    WithTargetAndTargetCollection bean = new WithTargetAndTargetCollection();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithTargetAndTargetCollection.class, bean.id);
    checkClass(document, WithTargetAndTargetCollection.class);
    checkClasses(document, WithTargetAndTargetCollection.class, Object.class);
    checkProperty(document, "WithTargetAndTargetCollection__id", bean.id);
    checkProperty(document, "WithTargetAndTargetCollection__collection",
        bean.collection);
  }

  @Test
  public void withNullCollection() {
    WithCollection bean = new WithCollection();
    bean.collection = null;
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    checkProperty(document, "WithCollection__collection", bean.collection);
  }

  @Test
  public void withEmptyCollection() {
    WithCollection bean = new WithCollection();
    bean.collection = Collections.emptySet();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    checkProperty(document, "WithCollection__collection", bean.collection);
  }

  @Test
  public void withSingleElementCollection() {
    WithCollection bean = new WithCollection();
    bean.collection = Collections.singleton("abc");
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    checkProperty(document, "WithCollection__collection", bean.collection);
  }

  @Test
  public void withNullElementCollection() {
    WithCollection bean = new WithCollection();
    bean.collection.clear();
    bean.collection.add("abc");
    bean.collection.add(null);
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    Iterator<String> iterator = bean.collection.iterator();
    String first = iterator.next();
    String second = iterator.next();
    checkProperty(document, "WithCollection__collection", new Object[] {
        first == null ? MultiValuedPropertySource.NULL_TRICK : first,
        second == null ? MultiValuedPropertySource.NULL_TRICK : second });
  }

  @Test
  public void withNullElementCollectionAndNoNullTrick() {
    WithCollection bean = new WithCollection();
    bean.collection.clear();
    bean.collection.add("abc");
    bean.collection.add(null);
    ExtendedDocumentObjectBinder binder = new ExtendedDocumentObjectBinderBuilder()
        .setNullTrickEnabled(false).build();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithCollection.class, bean.id);
    checkClass(document, WithCollection.class);
    checkClasses(document, WithCollection.class, Object.class);
    checkProperty(document, "WithCollection__id", bean.id);
    checkProperty(document, "WithCollection__collection", bean.collection);
  }
}
