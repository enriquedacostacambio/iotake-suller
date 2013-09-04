package com.iotake.solr.client.binder.itest.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.annotation.RetentionPolicy;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrEnumerated;
import com.iotake.solr.client.binder.annotation.SolrEnumerated.EnumType;
import com.iotake.solr.client.binder.annotation.SolrId;

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
    EasyDocument document = new EasyDocument(WithNameEnumerated.class, id,
        WithNameEnumerated.class, WithNameEnumerated.class, Object.class).set(
        "WithNameEnumerated__id", id).set("WithNameEnumerated__enumerated",
        enumerated.name());
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
    EasyDocument document = new EasyDocument(WithOrdinalEnumerated.class, id,
        WithOrdinalEnumerated.class, WithOrdinalEnumerated.class, Object.class)
        .set("WithOrdinalEnumerated__id", id).set(
            "WithOrdinalEnumerated__enumerated", enumerated.ordinal());
    Object object = binder.getBean(document);
    assertThat(object, CoreMatchers.instanceOf(WithOrdinalEnumerated.class));
    WithOrdinalEnumerated bean = (WithOrdinalEnumerated) object;
    assertEquals(id, bean.id);
    assertEquals(enumerated, bean.enumerated);
  }
}
