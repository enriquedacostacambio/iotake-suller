package com.iotake.suller.sullerj.session.basic;

import java.util.Deque;
import java.util.LinkedList;

import com.iotake.suller.sullerj.session.SessionCreator;
import com.iotake.suller.sullerj.session.WorkingSession;

public class ThreadBoundSessionFactory extends AbstractSessionFactory {

  private final ThreadLocal<Deque<WorkingSession>> sessions = new ThreadLocal<Deque<WorkingSession>>() {
    @Override
    protected Deque<WorkingSession> initialValue() {
      return new LinkedList<WorkingSession>();
    }
  };

  public ThreadBoundSessionFactory() {
    super();
  }

  public ThreadBoundSessionFactory(SessionCreator creator) {
    super(creator);
  }

  public WorkingSession begin(boolean requiresNew) {
    Deque<WorkingSession> threadSessions = sessions.get();
    if (requiresNew || threadSessions.isEmpty()) {
      WorkingSession session = create();
      threadSessions.push(session);
      return session;
    } else {
      WorkingSession session = threadSessions.peek();
      return session;
    }
  }

  public WorkingSession current() {
    Deque<WorkingSession> threadSessions = sessions.get();
    if (threadSessions.isEmpty()) {
      return null;
    }
    WorkingSession session = threadSessions.peek();
    return session;
  }

  public void closed(WorkingSession session) {
    Deque<WorkingSession> threadSessions = sessions.get();
    if (threadSessions.isEmpty()) {
      throw new IllegalStateException("No current session.");
    }
    WorkingSession currentSession = threadSessions.pop();
    if (session != currentSession) {
      threadSessions.push(currentSession);
      throw new IllegalStateException("Given session is not top session.");
    }
  }

}
