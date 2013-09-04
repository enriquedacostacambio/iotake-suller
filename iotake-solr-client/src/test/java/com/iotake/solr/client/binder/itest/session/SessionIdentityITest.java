package com.iotake.solr.client.binder.itest.session;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import com.iotake.solr.client.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.solr.client.binder.annotation.SolrDocument;
import com.iotake.solr.client.binder.annotation.SolrId;
import com.iotake.solr.client.binder.itest.BaseITest;
import com.iotake.solr.client.binder.session.basic.ThreadBoundSessionContext;

public class SessionIdentityITest extends BaseITest {

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinderBuilder().setSessionContext(
        new ThreadBoundSessionContext()).build();
  }

  @SolrDocument
  private static class Bean {

    @SolrId
    Long id;
  }

  private EasyDocument createDocument() {
    Long id = 123456789L;
    return new EasyDocument(Bean.class, id, Bean.class, Bean.class,
        Object.class).set("Bean__id", id);
  }

  private Bean createBean() {
    Bean bean = new Bean();
    bean.id = 123456789L;
    return bean;
  }

  @Test
  public void retrieveOnNoSession() {
    EasyDocument document = createDocument();
    Bean bean = binder.getBean(document);
    assertNotNull(bean);
  }

  @Test
  public void retrieveTwiceOnNoSession() {
    EasyDocument document = createDocument();
    Bean first = binder.getBean(document);
    assertNotNull(first);
    Bean second = binder.getBean(document);
    assertNotSame(first, second);
  }

  @Test
  public void retrieveTwiceOnSession() {
    binder.getSessionContext().begin(false);
    try {
      EasyDocument document = createDocument();
      Bean first = binder.getBean(document);
      assertNotNull(first);
      Bean second = binder.getBean(document);
      assertSame(first, second);

    } finally {
      binder.getSessionContext().close();
    }
  }

  @Test
  public void retrieveOnNestedSessions() {
    binder.getSessionContext().begin(false);
    try {
      EasyDocument document = createDocument();
      Bean outer = binder.getBean(document);
      assertNotNull(outer);
      binder.getSessionContext().begin(true);
      try {
        Bean inner = binder.getBean(document);
        assertNotSame(outer, inner);
      } finally {
        binder.getSessionContext().close();
      }

    } finally {
      binder.getSessionContext().close();
    }
  }

  @Test
  public void storeOnNoSession() {
    Bean stored = createBean();
    SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
    assertNotNull(inputDocument);
  }

  @Test
  public void storeAndRetrieveOnNoSession() {
    Bean stored = createBean();
    SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
    assertNotNull(inputDocument);
    EasyDocument document = createDocument();
    Bean retrieved = binder.getBean(document);
    assertNotSame(stored, retrieved);
  }

  @Test
  public void storeAndRetrieveOnSession() {
    binder.getSessionContext().begin(false);
    try {
      Bean stored = createBean();
      SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
      assertNotNull(inputDocument);
      EasyDocument document = createDocument();
      Bean retrieved = binder.getBean(document);
      assertSame(stored, retrieved);
    } finally {
      binder.getSessionContext().close();
    }
  }

  @Test
  public void storeAndRetrieveOnNestedSessions() {
    binder.getSessionContext().begin(false);
    try {
      Bean stored = createBean();
      SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
      assertNotNull(inputDocument);
      binder.getSessionContext().begin(true);
      try {
        EasyDocument document = createDocument();
        Bean retrieved = binder.getBean(document);
        assertNotSame(stored, retrieved);
      } finally {
        binder.getSessionContext().close();
      }
    } finally {
      binder.getSessionContext().close();
    }
  }
}
