package com.iotake.suller.sullerj.binder.itest.transfer;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEmbeddable;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.source.MultiValuedPropertySource;

public class ArrayTransferITest extends AbstractTransferITest {

  @SolrDocument
  private static class WithArray {
    @SolrId
    long id = 123;
    String[] array = { "abc", "def" };
  }

  @Test
  public void withArray() {
    WithArray bean = new WithArray();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", bean.array);
  }

  @SolrEmbeddable
  private static class ArrayEmbeddable {
    Integer[] embeddedArray = { 456, 789 };
  }

  @SolrDocument
  private static class WithEmbeddedArray {
    @SolrId
    long id = 123;
    ArrayEmbeddable embedded = new ArrayEmbeddable();
  }

  @Test
  public void withEmbeddedArray() {
    WithEmbeddedArray bean = new WithEmbeddedArray();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithEmbeddedArray.class, bean.id);
    checkClass(document, WithEmbeddedArray.class);
    checkClasses(document, WithEmbeddedArray.class, Object.class);
    checkProperty(document, "WithEmbeddedArray__id", bean.id);
    checkProperty(document, "WithEmbeddedArray__embedded__embeddedArray",
        bean.embedded.embeddedArray);
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
  private static class WithArrayOfEmbeddables {
    @SolrId
    long id = 123;
    SimpleEmbeddable[] array = new SimpleEmbeddable[] {
        new SimpleEmbeddable(23), new SimpleEmbeddable(45) };
  }

  @Test
  public void withArrayOfEmbeddables() {
    WithArrayOfEmbeddables bean = new WithArrayOfEmbeddables();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArrayOfEmbeddables.class, bean.id);
    checkClass(document, WithArrayOfEmbeddables.class);
    checkClasses(document, WithArrayOfEmbeddables.class, Object.class);
    checkProperty(document, "WithArrayOfEmbeddables__id", bean.id);
    checkProperty(document, "WithArrayOfEmbeddables__array__embeddedProperty",
        new Short[] { bean.array[0].embeddedProperty,
            bean.array[1].embeddedProperty });
  }

  @Test
  public void withNullArray() {
    WithArray bean = new WithArray();
    bean.array = null;
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", bean.array);
  }

  @Test
  public void withEmptyArray() {
    WithArray bean = new WithArray();
    bean.array = new String[0];
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", bean.array);
  }

  @Test
  public void withSingleElementArray() {
    WithArray bean = new WithArray();
    bean.array = new String[] { "abc" };
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", bean.array);
  }

  @Test
  public void withNullElementArray() {
    WithArray bean = new WithArray();
    bean.array = new String[] { "abc", null };
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", new Object[] { bean.array[0],
        MultiValuedPropertySource.NULL_TRICK });
  }

  @Test
  public void withNullElementArrayAndNoNullTrick() {
    binder = new ExtendedDocumentObjectBinderBuilder().setNullTrickEnabled(
        false).build();

    WithArray bean = new WithArray();
    bean.array = new String[] { "abc", null };
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithArray.class, bean.id);
    checkClass(document, WithArray.class);
    checkClasses(document, WithArray.class, Object.class);
    checkProperty(document, "WithArray__id", bean.id);
    checkProperty(document, "WithArray__array", bean.array);
  }

}
