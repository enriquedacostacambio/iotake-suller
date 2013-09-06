package com.iotake.suller.sullerj.binder.session.basic;

import com.iotake.suller.sullerj.binder.session.SessionCreator;
import com.iotake.suller.sullerj.binder.session.WorkingSession;
import com.iotake.suller.sullerj.binder.session.WorkingSessionFactory;

public abstract class AbstractSessionFactory implements WorkingSessionFactory {

  private final SessionCreator creator;

  public AbstractSessionFactory() {
    this(new DefaultSessionCreator());
  }

  public AbstractSessionFactory(SessionCreator creator) {
    this.creator = creator;
  }

  protected WorkingSession create() {
    return creator.create(this);
  }
}
