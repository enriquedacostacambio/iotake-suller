package com.iotake.solr.client.binder.session;

/**
 * Interface used internally by the binder. All sessions must implement this
 * rather than directly the Session interface.
 * 
 * @author enrque.dacostacambio
 * 
 */
public interface WorkingSession extends Session {

  /**
   * Registers a bean in the session.
   * 
   * @param id
   *          The bean id.
   * @param bean
   *          The bean.
   * @throws NullPointerException
   *           If id or bean are null.
   * @throws IllegalStateException
   *           If session is closed.
   */
  <T> void register(Object id, T bean);

}
