package eus.blankcard.decklearn.gamification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.TrainingSessionModel;

@Component
public class SessionManager {

  private Map<String, SessionCardManager> sessionRelation;

  // This is used to inject prototypeScope components into singleton components
  // https://www.baeldung.com/spring-inject-prototype-bean-into-singleton#javax_api
  @Autowired
  private ObjectFactory<SessionCardManager> sessionCardManagerFactory;

  public SessionManager() {
    sessionRelation = new HashMap<>();
  }

  public SessionCardManager addSession(String username, TrainingSessionModel trainingSession) {

    SessionCardManager sessionCardManager = sessionCardManagerFactory.getObject();
    sessionCardManager.initSessionCardManager(trainingSession);

    sessionRelation.put(username, sessionCardManager);

    return sessionCardManager;
  }

  public void removeSession(String username) {
    try {
      sessionRelation.remove(username);
    } catch (Exception e) {
    }
  }

  public SessionCardManager getSession(String username) {
    SessionCardManager sessionCardManager = null;

    try {
      sessionCardManager = sessionRelation.get(username);
    } catch (Exception e) {
    }

    return sessionCardManager;
  }
}
