package com.iotake.solr.client.binder.itest.extract;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrEmbeddable;
import com.iotake.solr.client.binder.annotation.SolrId;

public class ArrayExtractITest extends AbstractExtractITest {

  @SolrDocument
  private static class WithArray {
    @SolrId
    long id;
    String[] array;
  }

  @Test
  public void withArray() {
    long id = 123;
    String[] array = { "abc", "def" };
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", array);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertArrayEquals(array, bean.array);
  }

  @SolrEmbeddable
  private static class ArrayEmbeddable {
    Integer[] embeddedArray;
  }

  @SolrDocument
  private static class WithEmbeddedArray {
    @SolrId
    long id;
    ArrayEmbeddable embedded;
  }

  @Test
  public void withEmbeddedArray() {
    long id = 123;
    Integer[] embeddedArray = { 456, 789 };
    EasyDocument document = new EasyDocument(WithEmbeddedArray.class, id,
        WithEmbeddedArray.class, WithEmbeddedArray.class, Object.class).set(
        "WithEmbeddedArray__id", id).set(
        "WithEmbeddedArray__embedded__embeddedArray",
        Arrays.asList(embeddedArray));
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithEmbeddedArray.class));
    WithEmbeddedArray bean = (WithEmbeddedArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.embedded);
    assertArrayEquals(embeddedArray, bean.embedded.embeddedArray);
  }

  @SolrEmbeddable
  private static class SimpleEmbeddable {
    Short embeddedProperty;
  }

  @SolrDocument
  private static class WithArrayOfEmbeddables {
    @SolrId
    long id;
    SimpleEmbeddable[] array;
  }

  @Test
  public void withArrayOfEmbeddables() {
    long id = 123;
    Short[] embeddedProperties = new Short[] { (short) 23, (short) 45 };
    EasyDocument document = new EasyDocument(WithArrayOfEmbeddables.class, id,
        WithArrayOfEmbeddables.class, WithArrayOfEmbeddables.class,
        Object.class).set("WithArrayOfEmbeddables__id", id).set(
        "WithArrayOfEmbeddables__array__embeddedProperty",
        Arrays.asList(embeddedProperties));
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArrayOfEmbeddables.class));
    WithArrayOfEmbeddables bean = (WithArrayOfEmbeddables) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertEquals(2, bean.array.length);
    SimpleEmbeddable first = bean.array[0];
    assertNotNull(first);
    assertEquals(embeddedProperties[0], first.embeddedProperty);
    SimpleEmbeddable second = bean.array[1];
    assertEquals(embeddedProperties[1], second.embeddedProperty);
  }

  @Test
  public void withNullArray() {
    long id = 123;
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertEquals(0, bean.array.length);
  }

  @Test
  public void withEmptyArray() {
    long id = 123;
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertEquals(0, bean.array.length);
  }

  @Test
  public void withSingleElementArray() {
    long id = 123;
    String[] array = new String[] { "abc" };
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", Arrays.asList(array));
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertArrayEquals(array, bean.array);
  }

  @Test
  public void withNullElementArray() {
    long id = 123;
    String[] array = new String[] { "abc", null };
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", Arrays.asList(array));
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertArrayEquals(array, bean.array);
  }

  @Test
  public void withNullElementArrayAndNoNullTrick() {
    binder = new ExtendedDocumentObjectBinderBuilder().setNullTrickEnabled(
        false).build();

    long id = 123;
    String[] array = new String[] { "abc", null };
    EasyDocument document = new EasyDocument(WithArray.class, id,
        WithArray.class, WithArray.class, Object.class)
        .set("WithArray__id", id).set("WithArray__array", Arrays.asList(array));
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithArray.class));
    WithArray bean = (WithArray) object;
    assertEquals(id, bean.id);
    assertNotNull(bean.array);
    assertArrayEquals(array, bean.array);
  }
}
