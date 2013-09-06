package com.iotake.solr.client.binder.session;


public interface SessionCreator {

  WorkingSession create(WorkingSessionFactory factory);
}
