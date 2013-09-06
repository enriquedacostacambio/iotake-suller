package com.iotake.suller.sullerj.binder.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.annotation.RetentionPolicy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated.EnumType;

public class EnumeratedExtractITest extends AbstractExtractITest {

  @SolrDocument
  private static class WithNameEnumerated {
    @SolrId
    long id;
    RetentionPolicy enumerated;
  }

  @Test
  public void withNameEnumerated() {
    long id = 123;
    RetentionPolicy enumerated = RetentionPolicy.RUNTIME;
    EasyDocument document = new EasyDocument(id, WithNameEnumerated.class,
        WithNameEnumerated.class, Object.class).set("WithNameEnumerated__id",
        id).set("WithNameEnumerated__enumerated", enumerated.name());
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithNameEnumerated.class));
    WithNameEnumerated bean = (WithNameEnumerated) object;
    assertEquals(id, bean.id);
    assertEquals(enumerated, bean.enumerated);
  }

  @SolrDocument
  private static class WithOrdinalEnumerated {
    @SolrId
    long id;
    @SolrEnumerated(EnumType.ORDINAL)
    RetentionPolicy enumerated;
  }

  @Test
  public void withOrdinalEnumerated() {
    long id = 123;
    RetentionPolicy enumerated = RetentionPolicy.RUNTIME;
    EasyDocument document = new EasyDocument(id, WithOrdinalEnumerated.class,
        WithOrdinalEnumerated.class, Object.class).set(
        "WithOrdinalEnumerated__id", id).set(
        "WithOrdinalEnumerated__enumerated", enumerated.ordinal());
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithOrdinalEnumerated.class));
    WithOrdinalEnumerated bean = (WithOrdinalEnumerated) object;
    assertEquals(id, bean.id);
    assertEquals(enumerated, bean.enumerated);
  }
}
