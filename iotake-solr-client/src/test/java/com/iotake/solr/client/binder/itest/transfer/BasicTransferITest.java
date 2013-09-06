package com.iotake.solr.client.binder.itest.transfer;

import java.util.RandomAccess;

import org.apache.commons.logging.Log;
import org.apache.solr.client.solrj.beans.BindingException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.annotation.SolrName;
import com.iotake.solr.client.binder.annotation.SolrTarget;
import com.iotake.solr.client.binder.annotation.SolrTransient;
import com.iotake.solr.client.binder.annotation.SolrWritable;

public class BasicTransferITest extends AbstractTransferITest {
  @SolrDocument
  private static class Basic {
    @SolrId
    long id = 123;
    String property = "abc";
  }

  @Test
  public void basic() {
    Basic bean = new Basic();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, Basic.class, bean.id);
    checkClass(document, Basic.class);
    checkClasses(document, Basic.class, Object.class);
    checkProperty(document, "Basic__id", bean.id);
    checkProperty(document, "Basic__property", bean.property);
  }

  @SolrDocument
  private static class WithInterface implements RandomAccess {
    @SolrId
    long id = 123;
  }

  @Test
  public void withInterface() {
    WithInterface bean = new WithInterface();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 1);
    checkGlobalId(document, WithInterface.class, bean.id);
    checkClass(document, WithInterface.class);
    checkClasses(document, WithInterface.class, Object.class,
        RandomAccess.class);
    checkProperty(document, "WithInterface__id", bean.id);
  }

  @SolrDocument
  private static class WithSuperclass extends Basic {
    Integer additionalProperty = 456;
  }

  @Test
  public void withSuperclass() {
    WithSuperclass bean = new WithSuperclass();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 3);
    checkGlobalId(document, WithSuperclass.class, bean.id);
    checkClass(document, WithSuperclass.class);
    checkClasses(document, WithSuperclass.class, Basic.class, Object.class);
    checkProperty(document, "Basic__id", bean.id);
    checkProperty(document, "Basic__property", bean.property);
    checkProperty(document, "WithSuperclass__additionalProperty",
        bean.additionalProperty);
  }

  @SolrDocument
  @SolrName("OtherName")
  private static class Renamed {
    @SolrId
    @SolrName("objId")
    long id = 123;
    @SolrName("prop")
    String property = "abc";
  }

  @Test
  public void renamed() {
    Renamed bean = new Renamed();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, Renamed.class, bean.id);
    checkClass(document, Renamed.class);
    checkClasses(document, Renamed.class, Object.class);
    checkProperty(document, "OtherName__objId", bean.id);
    checkProperty(document, "OtherName__prop", bean.property);
  }

  @SolrDocument
  private static class WithNonWritable {
    @SolrId
    long id = 123;
    @SolrWritable(false)
    String property = "abc";
  }

  @Test
  public void withNonWritable() {
    WithNonWritable bean = new WithNonWritable();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 1);
    checkGlobalId(document, WithNonWritable.class, bean.id);
    checkClass(document, WithNonWritable.class);
    checkClasses(document, WithNonWritable.class, Object.class);
    checkProperty(document, "WithNonWritable__id", bean.id);
    checkNoProperty(document, "WithNonWritable__property");
  }

  @SolrDocument
  private static class WithTransient {
    @SolrId
    long id = 123;
    @SolrTransient
    String property = "abc";
  }

  @Test
  public void withTransient() {
    WithTransient bean = new WithTransient();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 1);
    checkGlobalId(document, WithTransient.class, bean.id);
    checkClass(document, WithTransient.class);
    checkClasses(document, WithTransient.class, Object.class);
    checkProperty(document, "WithTransient__id", bean.id);
    checkNoProperty(document, "WithTransient__property");
  }

  @SolrDocument
  private static class WithTarget {
    @SolrId
    long id = 123;
    @SolrTarget(String.class)
    CharSequence property = "abc";
  }

  @Test
  public void withTarget() {
    WithTarget bean = new WithTarget();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithTarget.class, bean.id);
    checkClass(document, WithTarget.class);
    checkClasses(document, WithTarget.class, Object.class);
    checkProperty(document, "WithTarget__id", bean.id);
    checkProperty(document, "WithTarget__property", bean.property);
  }

  @SolrDocument
  private static class WithNullableId {
    @SolrId
    Long id = null;

  }

  @Test(expected = BindingException.class)
  public void withNullId() {
    WithNullableId bean = new WithNullableId();
    binder.toSolrInputDocument(bean);
  }

  @SolrDocument
  private static class WithNoId {
  }

  @Test(expected = BindingException.class)
  public void withNoId() {
    WithNoId bean = new WithNoId();
    binder.toSolrInputDocument(bean);
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
    WithUnsupportedType bean = new WithUnsupportedType();
    binder.toSolrInputDocument(bean);
  }

  @Test
  public void withNullProperty() {
    Basic bean = new Basic();
    bean.property = null;
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, Basic.class, bean.id);
    checkClass(document, Basic.class);
    checkClasses(document, Basic.class, Object.class);
    checkProperty(document, "Basic__id", bean.id);
    checkProperty(document, "Basic__property", bean.property);
  }
}
