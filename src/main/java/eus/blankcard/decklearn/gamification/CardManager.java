package eus.blankcard.decklearn.gamification;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CardManager {

    private Map<String, SessionManager> sessionRelation;


    public CardManager() {

    }

    public void createSessionManager(String username, SessionManager session) {
        sessionRelation.put(username, session);
    }
}
