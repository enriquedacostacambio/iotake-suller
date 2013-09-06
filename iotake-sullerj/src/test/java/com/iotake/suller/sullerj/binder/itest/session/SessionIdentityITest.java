package com.iotake.suller.sullerj.binder.itest.session;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.itest.BaseITest;
import com.iotake.suller.sullerj.binder.session.Session;
import com.iotake.suller.sullerj.binder.session.basic.ThreadBoundSessionFactory;

public class SessionIdentityITest extends BaseITest {

  @Before
  public void createBinder() {
    binder = new ExtendedDocumentObjectBinderBuilder().setSessionFactory(
        new ThreadBoundSessionFactory()).build();
  }

  @SolrDocument
  private static class Bean {

    @SolrId
    Long id;
  }

  private EasyDocument createDocument() {
    Long id = 123456789L;
    return new EasyDocument(id, Bean.class, Bean.class, Object.class).set(
        "Bean__id", id);
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
    Session session = binder.getSessionFactory().begin(false);
    try {
      EasyDocument document = createDocument();
      Bean first = binder.getBean(document);
      assertNotNull(first);
      Bean second = binder.getBean(document);
      assertSame(first, second);

    } finally {
      session.close();
    }
  }

  @Test
  public void retrieveOnNestedSessions() {
    Session session = binder.getSessionFactory().begin(false);
    try {
      EasyDocument document = createDocument();
      Bean outer = binder.getBean(document);
      assertNotNull(outer);
      Session nestedSession = binder.getSessionFactory().begin(true);
      try {
        Bean inner = binder.getBean(document);
        assertNotSame(outer, inner);
      } finally {
        nestedSession.close();
      }

    } finally {
      session.close();
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
    Session session = binder.getSessionFactory().begin(false);
    try {
      Bean stored = createBean();
      SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
      assertNotNull(inputDocument);
      EasyDocument document = createDocument();
      Bean retrieved = binder.getBean(document);
      assertSame(stored, retrieved);
    } finally {
      session.close();
    }
  }

  @Test
  public void storeAndRetrieveOnNestedSessions() {
    Session session = binder.getSessionFactory().begin(false);
    try {
      Bean stored = createBean();
      SolrInputDocument inputDocument = binder.toSolrInputDocument(stored);
      assertNotNull(inputDocument);
      Session nestedSession = binder.getSessionFactory().begin(true);
      try {
        EasyDocument document = createDocument();
        Bean retrieved = binder.getBean(document);
        assertNotSame(stored, retrieved);
      } finally {
        nestedSession.close();
      }
    } finally {
      session.close();
    }
  }
}
