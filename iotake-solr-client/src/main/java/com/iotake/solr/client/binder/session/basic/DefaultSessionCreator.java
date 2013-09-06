package com.iotake.solr.client.binder.session.basic;

import com.iotake.solr.client.binder.session.WorkingSession;
import com.iotake.solr.client.binder.session.SessionCreator;
import com.iotake.solr.client.binder.session.WorkingSessionFactory;

public class DefaultSessionCreator implements SessionCreator {

  public WorkingSession create(WorkingSessionFactory factory) {
    return new DefaultSession(factory);
  }

}
