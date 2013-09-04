package com.iotake.solr.client.binder.itest.transfer;

import java.util.List;

import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrEmbeddable;
import com.iotake.solr.client.binder.annotation.SolrId;

public  class EmbeddedTransferITest extends AbstractTransferITest {

  @SolrEmbeddable
  private static class SimpleEmbeddable {
    Short embeddedProperty = 45;
  }

  @SolrDocument
  private static class WithEmbeddable {
    @SolrId
    long id = 123;
    SimpleEmbeddable embedded = new SimpleEmbeddable();
  }

  @Test
  public void withEmbeddable() {
    WithEmbeddable bean = new WithEmbeddable();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithEmbeddable.class, bean.id);
    checkClass(document, WithEmbeddable.class);
    checkClasses(document, WithEmbeddable.class, Object.class);
    checkProperty(document, "WithEmbeddable__id", bean.id);
    checkProperty(document, "WithEmbeddable__embedded__embeddedProperty",
        bean.embedded.embeddedProperty);
  }

  @Test
  public void withNullEmbeddable() {
    WithEmbeddable bean = new WithEmbeddable();
    bean.embedded = null;
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithEmbeddable.class, bean.id);
    checkClass(document, WithEmbeddable.class);
    checkClasses(document, WithEmbeddable.class, Object.class);
    checkProperty(document, "WithEmbeddable__id", bean.id);
    checkProperty(document, "WithEmbeddable__embedded__embeddedProperty",
        (Object) null);
  }

  @SolrEmbeddable
  private static class SimpleNotNullableEmbeddable {
    @SuppressWarnings("unused")
    short embeddedProperty = 45;
  }

  @SolrDocument
  private static class WithNotNullableEmbeddable {
    @SolrId
    long id = 123;
    @SuppressWarnings("unused")
    SimpleNotNullableEmbeddable embedded = new SimpleNotNullableEmbeddable();
  }

  @Test(expected = BindingException.class)
  public void withNullNotNullableEmbeddable() {
    WithNotNullableEmbeddable bean = new WithNotNullableEmbeddable();
    bean.embedded = null;
    binder.toSolrInputDocument(bean);
  }

  @SolrEmbeddable
  private static class NestingEmbeddable {
    SimpleEmbeddable nested = new SimpleEmbeddable();
  }

  @SolrDocument
  private static class WithNestedEmbeddable {
    @SolrId
    long id = 123;
    NestingEmbeddable nesting = new NestingEmbeddable();
  }

  @Test
  public void withNestedEmbeddable() {
    WithNestedEmbeddable bean = new WithNestedEmbeddable();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithNestedEmbeddable.class, bean.id);
    checkClass(document, WithNestedEmbeddable.class);
    checkClasses(document, WithNestedEmbeddable.class, Object.class);
    checkProperty(document, "WithNestedEmbeddable__id", bean.id);
    checkProperty(document,
        "WithNestedEmbeddable__nesting__nested__embeddedProperty",
        bean.nesting.nested.embeddedProperty);
  }

  @SolrEmbeddable
  private static class WithMultiValuedEmbeddable {
    @SuppressWarnings("unused")
    Integer[] embeddedArray;
  }

  @SolrDocument
  private static class WithNestedMultiValued {
    @SolrId
    long id;
    @SuppressWarnings("unused")
    List<WithMultiValuedEmbeddable> collection;
  }

  @Test(expected = BindingException.class)
  public void withNestedMultiValued() {
    WithNestedMultiValued bean = new WithNestedMultiValued();
    binder.toSolrInputDocument(bean);
  }

}
