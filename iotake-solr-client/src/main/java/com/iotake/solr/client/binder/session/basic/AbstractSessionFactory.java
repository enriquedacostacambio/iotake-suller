package com.iotake.solr.client.binder.session.basic;

import com.iotake.solr.client.binder.session.SessionCreator;
import com.iotake.solr.client.binder.session.WorkingSession;
import com.iotake.solr.client.binder.session.WorkingSessionFactory;

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
