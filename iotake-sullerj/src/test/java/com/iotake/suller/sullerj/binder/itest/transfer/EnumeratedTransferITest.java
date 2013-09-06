package com.iotake.suller.sullerj.binder.itest.transfer;

import java.lang.annotation.RetentionPolicy;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrEnumerated.EnumType;

public  class EnumeratedTransferITest extends AbstractTransferITest {

  @SolrDocument
  private static class WithNameEnumerated {
    @SolrId
    long id = 123;
    RetentionPolicy enumerated = RetentionPolicy.RUNTIME;
  }

  @Test
  public void withNameEnumerated() {
    WithNameEnumerated bean = new WithNameEnumerated();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithNameEnumerated.class, bean.id);
    checkClass(document, WithNameEnumerated.class);
    checkClasses(document, WithNameEnumerated.class, Object.class);
    checkProperty(document, "WithNameEnumerated__id", bean.id);
    checkProperty(document, "WithNameEnumerated__enumerated",
        bean.enumerated.name());
  }

  @SolrDocument
  private static class WithOrdinalEnumerated {
    @SolrId
    long id = 123;
    @SolrEnumerated(EnumType.ORDINAL)
    RetentionPolicy enumerated = RetentionPolicy.RUNTIME;
  }

  @Test
  public void withOrdinalEnumerated() {
    WithOrdinalEnumerated bean = new WithOrdinalEnumerated();
    SolrInputDocument document = binder.toSolrInputDocument(bean);
    checkFieldCount(document, 2);
    checkGlobalId(document, WithOrdinalEnumerated.class, bean.id);
    checkClass(document, WithOrdinalEnumerated.class);
    checkClasses(document, WithOrdinalEnumerated.class, Object.class);
    checkProperty(document, "WithOrdinalEnumerated__id", bean.id);
    checkProperty(document, "WithOrdinalEnumerated__enumerated",
        bean.enumerated.ordinal());
  }
}
