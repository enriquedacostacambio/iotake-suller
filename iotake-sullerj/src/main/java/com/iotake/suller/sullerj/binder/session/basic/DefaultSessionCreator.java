package com.iotake.suller.sullerj.binder.session.basic;

import com.iotake.suller.sullerj.binder.session.SessionCreator;
import com.iotake.suller.sullerj.binder.session.WorkingSession;
import com.iotake.suller.sullerj.binder.session.WorkingSessionFactory;

public class DefaultSessionCreator implements SessionCreator {

  public WorkingSession create(WorkingSessionFactory factory) {
    return new DefaultSession(factory);
  }

}
