package eus.blankcard.decklearn.gamification;

import java.util.Map;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.TrainingSessionModel;

@Component
public class SessionManager {

    private Map<String, SessionCardManager> sessionRelation;


    public SessionManager() {

    }

    public void createSessionManager(String username, SessionCardManager session) {
        sessionRelation.put(username, session);
    }

    public void addSession(String username, TrainingSessionModel trainingSession) {
        SessionCardManager sessionManager = new SessionCardManager(trainingSession);
        sessionRelation.put(username, sessionManager);
    }
}
