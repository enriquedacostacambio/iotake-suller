package com.iotake.suller.sullerj.binder.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.RandomAccess;

import org.apache.commons.logging.Log;
import org.apache.solr.client.solrj.beans.BindingException;
import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrName;
import com.iotake.suller.sullerj.binder.annotation.SolrReadable;
import com.iotake.suller.sullerj.binder.annotation.SolrTarget;
import com.iotake.suller.sullerj.binder.annotation.SolrTargetCollection;
import com.iotake.suller.sullerj.binder.annotation.SolrTransient;

public class BasicExtractITest extends AbstractExtractITest {

  @SolrDocument
  private static class Basic {
    @SolrId
    long id;
    String property;
  }

  @Test
  public void basic() {
    long id = 123;
    String property = "abc";
    EasyDocument document = new EasyDocument(id, Basic.class, Basic.class,
        Object.class).set("Basic__id", id).set("Basic__property", property);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(Basic.class));
    Basic bean = (Basic) object;
    assertEquals(id, bean.id);
    assertEquals(property, bean.property);
  }

  @SolrDocument
  private static class WithInterface implements RandomAccess {
    @SolrId
    long id;
  }

  @Test
  public void withInterface() {
    long id = 123;
    EasyDocument document = new EasyDocument(id, WithInterface.class,
        WithInterface.class, Object.class, RandomAccess.class).set(
        "WithInterface__id", id);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithInterface.class));
    WithInterface bean = (WithInterface) object;
    assertEquals(id, bean.id);
  }

  @SolrDocument
  private static class WithSuperclass extends Basic {
    Integer additionalProperty;
  }

  @Test
  public void withSuperclass() {
    long id = 123;
    String property = "abc";
    Integer additionalProperty = 456;
    EasyDocument document = new EasyDocument(id, WithSuperclass.class,
        WithSuperclass.class, Basic.class, Object.class).set("Basic__id", id)
        .set("Basic__property", property)
        .set("WithSuperclass__additionalProperty", additionalProperty);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithSuperclass.class));
    WithSuperclass bean = (WithSuperclass) object;
    assertEquals(id, bean.id);
    assertEquals(property, bean.property);
    assertEquals(additionalProperty, bean.additionalProperty);
  }

  @SolrDocument
  @SolrName("OtherName")
  private static class Renamed {
    @SolrId
    @SolrName("objId")
    long id;
    @SolrName("prop")
    String property;
  }

  @Test
  public void renamed() {
    long id = 123;
    String property = "abc";
    EasyDocument document = new EasyDocument(id, Renamed.class, Renamed.class,
        Object.class).set("OtherName__objId", id).set("OtherName__prop",
        property);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(Renamed.class));
    Renamed bean = (Renamed) object;
    assertEquals(id, bean.id);
    assertEquals(property, bean.property);
  }

  @SolrDocument
  private static class WithNonReadable {
    @SolrId
    long id;
    @SolrReadable(false)
    String property;
  }

  @Test
  public void withNonReadable() {
    long id = 123;
    String property = "abc";
    EasyDocument document = new EasyDocument(id, WithNonReadable.class,
        WithNonReadable.class, Object.class).set("WithNonReadable__id", id)
        .set("WithNonReadable__property", property);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithNonReadable.class));
    WithNonReadable bean = (WithNonReadable) object;
    assertEquals(id, bean.id);
    assertNull(bean.property);
  }

  @SolrDocument
  private static class WithTransient {
    @SolrId
    long id;
    @SolrTransient
    String property;
  }

  @Test
  public void withTransient() {
    long id = 123;
    String property = "abc";
    EasyDocument document = new EasyDocument(id, WithTransient.class,
        WithTransient.class, Object.class).set("WithTransient__id", id).set(
        "WithTransient__property", property);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithTransient.class));
    WithTransient bean = (WithTransient) object;
    assertEquals(id, bean.id);
    assertNull(bean.property);
  }

  @SolrDocument
  private static class WithTarget {
    @SolrId
    long id;
    @SolrTarget(String.class)
    CharSequence property;
  }

  @Test
  public void withTarget() {
    long id = 123;
    String property = "abc";
    EasyDocument document = new EasyDocument(id, WithTarget.class,
        WithTarget.class, Object.class).set("WithTarget__id", id).set(
        "WithTarget__property", property);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithTarget.class));
    WithTarget bean = (WithTarget) object;
    assertEquals(id, bean.id);
    assertThat(bean.property, CoreMatchers.instanceOf(String.class));
    assertEquals(property, bean.property);
  }

  @SolrDocument
  private static class WithTargetCollection {
    @SolrId
    long id;
    @SolrTargetCollection(LinkedList.class)
    Collection<String> collection;
  }

  @SolrDocument
  private static class WithNullableId {
    @SolrId
    Long id;

  }

  @Ignore
  @Test(expected = BindingException.class)
  public void withNullId() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(id, WithNullableId.class,
        WithNullableId.class, Object.class).set("WithNullableId__id", null);
    binder.getBean(document);
  }

  @Test(expected = BindingException.class)
  public void withNullGloblaId() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(null, null, WithNullableId.class,
        WithNullableId.class, Object.class).set("WithNullableId_id", id);
    binder.getBean(document);
  }

  @Test(expected = BindingException.class)
  public void withNullClass() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(id, WithNullableId.class,
        WithNullableId.class, Object.class).setBeanClass(null).set(
        "WithNullableId_id", id);
    binder.getBean(document);
  }

  @Test
  public void withNullClasses() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(id, WithNullableId.class,
        (Class<?>[]) null).set("WithNullableId_id", id);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithNullableId.class));
    WithNullableId bean = (WithNullableId) object;
    assertEquals(id, bean.id);
  }

  @SolrDocument
  private static class WithNoId {
  }

  @Test(expected = BindingException.class)
  public void withNoId() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(id, WithNoId.class,
        WithNoId.class, Object.class);
    binder.getBean(document);
  }

  @SolrDocument
  private static class WithUnsupportedType {
    @SolrId
    long id = 123;
    @SuppressWarnings("unused")
    Log unsupported;

  }

  @Test(expected = BindingException.class)
  public void withUnsupportedType() {
    Long id = 123L;
    EasyDocument document = new EasyDocument(id, WithUnsupportedType.class,
        WithUnsupportedType.class, Object.class);
    binder.getBean(document);
  }

  @Test
  public void withNullProperty() {
    long id = 123;
    EasyDocument document = new EasyDocument(id, Basic.class, Basic.class,
        Object.class).set("Basic__id", id).set("Basic__property", null);
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(Basic.class));
    Basic bean = (Basic) object;
    assertEquals(id, bean.id);
    assertNull(bean.property);
  }

}
