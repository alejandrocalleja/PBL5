package eus.blankcard.decklearn.gamification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import eus.blankcard.decklearn.models.CardModel;
import eus.blankcard.decklearn.models.CardResponseModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.repository.CardResponseRepository;
import eus.blankcard.decklearn.repository.TrainingSessionRepository;

public class SessionCardManager implements Runnable {
    private TrainingSessionModel trainingSession;
    private Buffer buffer;
    private List<CardModel> cards;
    private Map<Integer, List<CardResponseModel>> resultResponseMap;
    private int index;

    @Autowired
    CardResponseRepository cardResponseRepository;

    @Autowired
    TrainingSessionRepository trainingSessionRepository;

    public SessionCardManager(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;

        buffer = new Buffer();
        index = 0;
        initMap();
    }
    
    private void initMap() {
        resultResponseMap = new HashMap<>();
        cards.forEach(c -> resultResponseMap.put(c.getCard_id(), new ArrayList<>()));
    }

    private void saveSessionResults() {
        resultResponseMap.forEach((k, v) -> {

        });
    }

    public TrainingSessionModel getTrainingSessionId() {
        return trainingSession;
    }

    public void setTrainingSessionId(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void saveCardResponse(CardResponseModel cardResponse) {
        // Si el cardResponse es correcto se guarda y se borra la carta de la lista de las posibilidades

    }

    public CardModel getNextCard() {
        CardModel card = null;

        if(index > cards.size() - 1) {
            card = cards.get(index);
        }
        return card;
    }

    public void setCards(List<CardModel> cards) {
        this.cards = cards;
    }

    @Override
    public void run() {
        // cards.forEach(c -> {
        //     try {
        //         buffer.putValue(c);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // });
    }
}
