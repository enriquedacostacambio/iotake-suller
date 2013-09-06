package com.iotake.suller.sullerj.session.basic;

import com.iotake.suller.sullerj.session.WorkingSession;
import com.iotake.suller.sullerj.session.WorkingSessionFactory;

public abstract class AbstractSession implements WorkingSession {

  private final WorkingSessionFactory factory;

  public AbstractSession(WorkingSessionFactory factory) {
    this.factory = factory;
  }

  public void close() {
    factory.closed(this);
    doClose();
  }

  protected abstract void doClose();

}
