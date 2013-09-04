package com.iotake.solr.client.binder.server;

import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

/**
 * Test for {@link DocumentBinderInjector}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class DocumentBinderInjectorTest {

  private static class TestSolrServer extends SolrServer {

    private static final long serialVersionUID = 1L;

    @Override
    public void shutdown() {

    }

    @Override
    public NamedList<Object> request(SolrRequest request)
        throws SolrServerException, IOException {
      return new NamedList<Object>();
    }
  }

  @Test
  public void construct() {
    SolrServer server = new TestSolrServer();
    DocumentObjectBinder binder = new DocumentObjectBinder();
    new DocumentBinderInjector(server, binder);
    assertSame(binder, server.getBinder());
  }

  @Test
  public void inject() {
    SolrServer server = new TestSolrServer();
    DocumentObjectBinder binder = new DocumentObjectBinder();
    DocumentBinderInjector.inject(server, binder);
    assertSame(binder, server.getBinder());
  }

  @Test
  public void injectWithAlreadyInitializedBinder() {
    SolrServer server = new TestSolrServer();
    server.getBinder();
    DocumentObjectBinder binder = new DocumentObjectBinder();
    DocumentBinderInjector.inject(server, binder);
    assertSame(binder, server.getBinder());
  }

  @Test(expected = NullPointerException.class)
  public void injectWithNullServer() {
    SolrServer server = null;
    DocumentObjectBinder binder = new DocumentObjectBinder();
    DocumentBinderInjector.inject(server, binder);
  }

  @Test(expected = NullPointerException.class)
  public void injectWithNullBinder() {
    SolrServer server = new TestSolrServer();
    DocumentObjectBinder binder = null;
    DocumentBinderInjector.inject(server, binder);
  }
}
