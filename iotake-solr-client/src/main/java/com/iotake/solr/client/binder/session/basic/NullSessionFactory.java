package com.iotake.solr.client.binder.session.basic;

import com.iotake.solr.client.binder.session.WorkingSession;
import com.iotake.solr.client.binder.session.WorkingSessionFactory;

public class NullSessionFactory implements WorkingSessionFactory {

  public static final NullSessionFactory INSTANCE = new NullSessionFactory();

  private static class NullSession implements WorkingSession {

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

    public void close() {

    }

  }

  private NullSessionFactory() {
  }

  public WorkingSession begin(boolean requiresNew) {
    return NullSession.INSTANCE;
  }

  public WorkingSession current() {
    return NullSession.INSTANCE;
  }

  public void closed(WorkingSession session) {

  }

}
