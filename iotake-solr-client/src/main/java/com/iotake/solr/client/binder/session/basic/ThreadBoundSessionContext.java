package com.iotake.solr.client.binder.session.basic;

import java.util.Deque;
import java.util.LinkedList;

import com.iotake.solr.client.binder.session.Session;
import com.iotake.solr.client.binder.session.SessionContext;

public class ThreadBoundSessionContext implements SessionContext {

  private final ThreadLocal<Deque<SessionImpl>> sessions = new ThreadLocal<Deque<SessionImpl>>() {
    @Override
    protected Deque<SessionImpl> initialValue() {
      return new LinkedList<SessionImpl>();
    }
  };

  public Session begin(boolean requiresNew) {
    Deque<SessionImpl> threadSessions = sessions.get();
    if (requiresNew || threadSessions.isEmpty()) {
      SessionImpl session = new SessionImpl();
      threadSessions.push(session);
      return session;
    } else {
      SessionImpl session = threadSessions.peek();
      return session;
    }
  }

  public Session current() {
    Deque<SessionImpl> threadSessions = sessions.get();
    if (threadSessions.isEmpty()) {
      return null;
    }
    SessionImpl session = threadSessions.peek();
    return session;
  }

  public void close() {
    Deque<SessionImpl> threadSessions = sessions.get();
    if (threadSessions.isEmpty()) {
      throw new IllegalStateException("No current session");
    }
    SessionImpl session = threadSessions.pop();
    session.close();
  }

}
