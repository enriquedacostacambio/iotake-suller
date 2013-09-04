package com.iotake.solr.client.binder.session.impl;

import com.iotake.solr.client.binder.session.Session;
import com.iotake.solr.client.binder.session.SessionContext;

public class NullSessionContext implements SessionContext {

  public static final NullSessionContext INSTANCE = new NullSessionContext();

  private static class NullSession implements Session {

    private static final NullSession INSTANCE = new NullSession();

    private NullSession() {
    }

    public <T> T lookup(Class<T> beanClass, Object id) {
      return null;
    }

    public <T> void register(Object id, T bean) {
    }

    public <T> void evict(Class<T> beanClass, Object id) {

    }

    public void clear() {

    }

  }

  private NullSessionContext() {
  }

  public Session begin(boolean requiresNew) {
    return NullSession.INSTANCE;
  }

  public Session current() {
    return NullSession.INSTANCE;
  }

  public void close() {

  }

}
