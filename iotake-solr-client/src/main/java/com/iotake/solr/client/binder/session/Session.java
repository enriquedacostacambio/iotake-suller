package com.iotake.solr.client.binder.session;

/**
 * Binder session
 * 
 * @author enrque.dacostacambio
 * 
 */
public interface Session {

  /**
   * Obtains the bean instance registered with given class and id.
   * 
   * @param beanClass
   *          The bean class.
   * @param id
   *          The bean id.
   * @return The bean or null if not registered.
   * @throws NullPointerException
   *           If class or id are null.
   * @throws IllegalStateException
   *           If session is closed.
   */
  <T> T lookup(Class<T> beanClass, Object id);

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

  /**
   * Removes a bean from the session. Meant for long running sessions. Use
   * carefully.
   * 
   * @param beanClass
   *          The bean class.
   * @param id
   *          The bean id.
   * @throws NullPointerException
   *           If class or id are null.
   * @throws IllegalStateException
   *           If session is closed.
   */
  <T> void evict(Class<T> beanClass, Object id);

  /**
   * Removes all bean from the session. Meant for long running sessions. Use
   * carefully.
   * 
   * @throws IllegalStateException
   *           If session is closed.
   */
  void clear();

}
