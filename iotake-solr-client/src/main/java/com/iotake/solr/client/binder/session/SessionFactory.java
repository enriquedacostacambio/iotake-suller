package com.iotake.solr.client.binder.session;

/**
 * Provides access to a context session that is capable of maintaining object
 * identity.
 * 
 * @author enrque.dacostacambio
 * 
 */
public interface SessionFactory {

  /**
   * Begins a session. If requiresNew is false it could retrieve an already
   * existing session if one is available. If requiresNew is true it will return
   * a nested session if one was already available.
   * 
   * @param requiresNew
   *          Whether the session must be new or could be a existing session.
   * @return The session
   */
  Session begin(boolean requiresNew);

  /**
   * Retrieves the current session if one is available or null if none.
   * 
   * @return The session or null.
   */
  Session current();

}
