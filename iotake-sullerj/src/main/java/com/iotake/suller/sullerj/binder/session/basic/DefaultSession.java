package com.iotake.suller.sullerj.binder.session.basic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.iotake.suller.sullerj.binder.session.WorkingSessionFactory;

public class DefaultSession extends AbstractSession {

  public DefaultSession(WorkingSessionFactory factory) {
    super(factory);
  }

  private static class Key {
    private final Class<?> beanClass;
    private final Object id;
    private final int hashCode;

    public Key(Class<?> beanClass, Object id) {
      checkNotNull(beanClass, "Bean class cannot be null");
      checkNotNull(beanClass, "Bean id cannot be null");
      this.beanClass = beanClass;
      this.id = id;
      this.hashCode = new HashCodeBuilder().append(beanClass).append(id)
          .toHashCode();
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof Key)) {
        return false;
      }
      Key pair = (Key) other;
      return this.beanClass.equals(pair.beanClass) && this.id.equals(pair.id);
    }

  }

  private boolean closed = false;
  private final Map<Key, Object> beans = new HashMap<Key, Object>();

  public <T> T lookup(Class<T> beanClass, Object id) {
    checkNotClosed();
    Key key = new Key(beanClass, id);
    Object bean = beans.get(key);
    return beanClass.cast(bean);
  }

  public <T> void register(Object id, T bean) {
    checkNotClosed();
    checkNotNull(bean, "Bean cannot be null");
    Class<?> beanClass = bean.getClass();
    Key key = new Key(beanClass, id);
    Object registeredBean = beans.put(key, bean);
    if (registeredBean != null && registeredBean != bean) {
      beans.put(key, registeredBean);
      throw new IllegalStateException(
          "Another instance already registered for that bean class and id. Class: "
              + beanClass.getName() + ", id: " + id + ", registered bean "
              + registeredBean + ", new bean:" + bean);
    }
  }

  public <T> void evict(Class<T> beanClass, Object id) {
    checkNotClosed();
    Key key = new Key(beanClass, id);
    beans.remove(key);
  }

  public void clear() {
    checkNotClosed();
    beans.clear();
  }

  public void doClose() {
    clear();
    closed = true;
  }

  protected void checkNotClosed() {
    if (closed) {
      throw new IllegalStateException("Session closed.");
    }
  }

}
