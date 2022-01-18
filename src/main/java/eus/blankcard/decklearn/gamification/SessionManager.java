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

    public SessionCardManager addSession(String username, TrainingSessionModel trainingSession) {
        System.out.println("Adding the session" + trainingSession.getId() + "of " + username + " to the session manager");
        SessionCardManager sessionManager = new SessionCardManager(trainingSession);
        sessionRelation.put(username, sessionManager);

        return sessionManager;
    }

    public void removeSession(String username) {
        try {
            sessionRelation.remove(username);
        } catch (Exception e) {
            System.out.println("Not able to remove session from " + username);
            System.out.println("No such a relation exists.");
        }
    }

    public SessionCardManager getSession(String username) {
        SessionCardManager sessionCardManager = null;

        try {
            sessionCardManager = sessionRelation.get(username);
        } catch (Exception e) {
            System.out.println("Not able to get the session from " + username);
        }

        return sessionCardManager;
    }
}
