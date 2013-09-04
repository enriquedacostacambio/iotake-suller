package com.iotake.solr.client.binder.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.solr.client.solrj.beans.BindingException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrEmbeddable;
import com.iotake.solr.client.binder.annotation.SolrId;

public class EmbeddedExtractITest extends AbstractExtractITest {

  @SolrEmbeddable
  private static class SimpleEmbeddable {
    Short embeddedProperty;
  }

  @SolrDocument
  private static class WithEmbeddable {
    @SolrId
    long id;
    SimpleEmbeddable embedded;
  }

  @Test
  public void withEmbeddable() {
    long id = 123;
    Short embeddedProperty = 45;
    EasyDocument document = new EasyDocument(WithEmbeddable.class, id,
        WithEmbeddable.class, WithEmbeddable.class, Object.class).set(
        "WithEmbeddable__id", id).set(
        "WithEmbeddable__embedded__embeddedProperty", embeddedProperty);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithEmbeddable.class));
    WithEmbeddable bean = (WithEmbeddable) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.embedded);
    assertEquals(embeddedProperty, bean.embedded.embeddedProperty);
  }

  @Test
  public void withNullEmbeddable() {
    long id = 123;
    EasyDocument document = new EasyDocument(WithEmbeddable.class, id,
        WithEmbeddable.class, WithEmbeddable.class, Object.class).set(
        "WithEmbeddable__id", id).set(
        "WithEmbeddable__embedded__embeddedProperty", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithEmbeddable.class));
    WithEmbeddable bean = (WithEmbeddable) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.embedded);
    assertNull(bean.embedded.embeddedProperty);
  }

  @SolrEmbeddable
  private static class SimpleNotNullableEmbeddable {
    @SuppressWarnings("unused")
    short embeddedProperty;
  }

  @SolrDocument
  private static class WithNotNullableEmbeddable {
    @SolrId
    long id;
    @SuppressWarnings("unused")
    SimpleNotNullableEmbeddable embedded;
  }

  @Test(expected = BindingException.class)
  public void withNullNotNullableEmbeddable() {
    long id = 123;
    EasyDocument document = new EasyDocument(WithNotNullableEmbeddable.class,
        id, WithNotNullableEmbeddable.class, WithNotNullableEmbeddable.class,
        Object.class).set("WithNotNullableEmbeddable__id", id).set(
        "WithNotNullableEmbeddable__embedded__embeddedProperty", null);
    binder.getBean(document);
  }

  @SolrEmbeddable
  private static class NestingEmbeddable {
    SimpleEmbeddable nested;
  }

  @SolrDocument
  private static class WithNestedEmbeddable {
    @SolrId
    long id;
    NestingEmbeddable nesting;
  }

  @Test
  public void withNestedEmbeddable() {
    long id = 123;
    Short embeddedProperty = 45;
    EasyDocument document = new EasyDocument(WithNestedEmbeddable.class, id,
        WithNestedEmbeddable.class, WithNestedEmbeddable.class, Object.class)
        .set("WithNestedEmbeddable__id", id).set(
            "WithNestedEmbeddable__nesting__nested__embeddedProperty",
            embeddedProperty);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithNestedEmbeddable.class));
    WithNestedEmbeddable bean = (WithNestedEmbeddable) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.nesting);
    assertNotNull(bean.nesting.nested);
    assertNotNull(bean.nesting.nested);
    assertEquals(embeddedProperty, bean.nesting.nested.embeddedProperty);
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
    long id = 123;
    EasyDocument document = new EasyDocument(WithNestedMultiValued.class, id,
        WithNestedMultiValued.class, WithNestedMultiValued.class, Object.class)
        .set("WithNestedMultiValued__id", id);
    binder.getBean(document);
  }

}
