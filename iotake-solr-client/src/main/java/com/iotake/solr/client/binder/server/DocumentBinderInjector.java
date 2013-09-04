package com.iotake.solr.client.binder.server;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;

/**
 * Helper class to set the read-only {@link SolrServer#binder} property. This
 * could be achieved by subclassing every single SolrServer class and overriding
 * the {@link SolrServer#getBinder()} method.
 * 
 * @author enrique.dacostacambio
 * 
 */
public class DocumentBinderInjector {

  private static final String BINDER_FIELD_NAME = "binder";

  /**
   * Constructor that calls {@link #inject(SolrServer, DocumentObjectBinder)} on
   * creation. This is a convenience constructor to provide yet another
   * (easier?) way to configure as a Spring bean.
   * 
   * @param server
   *          The server to be altered. Cannot be null.
   * @param binder
   *          The binder to be set. Cannot be null.
   * @throws IllegalArgumentException
   *           if server or binder are null.
   * @throws UnsupportedOperationException
   *           if unable to find, access or set the field.
   */
  public DocumentBinderInjector(SolrServer server, DocumentObjectBinder binder) {
    inject(server, binder);
  }

  /**
   * Sets the given binder to the given server's {@link SolrServer#binder}
   * 
   * @param server
   *          The server to be altered. Cannot be null.
   * @param binder
   *          The binder to be set. Cannot be null.
   * @return the server.
   * @throws IllegalArgumentException
   *           if server or binder are null.
   * @throws UnsupportedOperationException
   *           if unable to find, access or set the field.
   */
  public static SolrServer inject(SolrServer server, DocumentObjectBinder binder) {
    checkNotNull(server, "Server cannot be null.");
    checkNotNull(binder, "Binder cannot be null.");
    Field binderField;
    try {
      binderField = SolrServer.class.getDeclaredField(BINDER_FIELD_NAME);
      binderField.setAccessible(true);
      binderField.set(server, binder);
    } catch (NoSuchFieldException e) {
      throw new UnsupportedOperationException(
          "Impossible to set binder. Field not present in server. New Solr version?",
          e);
    } catch (SecurityException e) {
      throw new UnsupportedOperationException(
          "Not allowed to set server's binder field.", e);
    } catch (IllegalAccessException e) {
      throw new UnsupportedOperationException(
          "Cannot access server's binder field.", e);
    }
    return server;
  }
}
