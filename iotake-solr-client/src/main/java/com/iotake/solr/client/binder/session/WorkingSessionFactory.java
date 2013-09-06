package com.iotake.solr.client.binder.session;


/**
 * Interface used internally by the binder. All sessions must implement this
 * rather than directly the Sessionfactory interface.
 * 
 * @author enrque.dacostacambio
 * 
 */
public interface WorkingSessionFactory extends SessionFactory {

  WorkingSession begin(boolean requiresNew);

  WorkingSession current();

  void closed(WorkingSession abstractSession);
}
