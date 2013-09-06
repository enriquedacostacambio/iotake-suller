package com.iotake.suller.sullerj.session.basic;

import com.iotake.suller.sullerj.session.SessionCreator;
import com.iotake.suller.sullerj.session.WorkingSession;
import com.iotake.suller.sullerj.session.WorkingSessionFactory;

public class DefaultSessionCreator implements SessionCreator {

  public WorkingSession create(WorkingSessionFactory factory) {
    return new DefaultSession(factory);
  }

}
