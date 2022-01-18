package eus.blankcard.decklearn.gamification;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;

import eus.blankcard.decklearn.models.CardModel;
import eus.blankcard.decklearn.models.CardResponseModel;
import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.repository.CardResponseRepository;
import eus.blankcard.decklearn.repository.ResultsRepository;
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

    @Autowired
    ResultsRepository resultsRepository;

    public SessionCardManager(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;

        System.out.println("Creating a new SessionCardManager for trainingSession " + trainingSession.getId());

        buffer = new Buffer();
        index = 0;
        loadCards();
        initMap();
    }
    
    private void loadCards() {
        this.cards = trainingSession.getTraining().getDeck().getCards();
        System.out.println(cards.size() + " cards loaded for the session.");
    }

    private void initMap() {
        resultResponseMap = new HashMap<>();
        cards.forEach(c -> resultResponseMap.put(c.getId(), new ArrayList<>()));
    }

    private void saveSessionResults() {
        AtomicInteger idGenerator = new AtomicInteger(1);
        resultResponseMap.forEach((k, v) -> {
            ResultsModel result = new ResultsModel();
            result.setTrainingSession(trainingSession);
            result.setId(idGenerator.getAndIncrement());
            resultsRepository.save(result);
            
            v.forEach(response -> {
                response.setResult(result);
                cardResponseRepository.save(response);
            });
            result.setCardResponses(v);
            // Mirar el anterior que caja es. Si no existe anterior a la caja 1, si existe y es a la primera bien
            // Anterior caja + 1 y si está mal directamente a la 1.
            if(v.size() > 1) {
                result.setBoxNumber(1);
                
            } else {
                // Calcularlo
                
            }

            result.setErrorCount(v.size());
            result.setAvgTime(calculateAvgTime(v));
        });
    }

    private Time calculateAvgTime(List<CardResponseModel> v) {
        // Todo a segundos, sacar AVG de eso y pasarlo otra vez a formato

        v.forEach(cardResponse -> {
            // cardResponse.getre
        });

        return null;
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

    public void saveCardResponse(CardModel card, boolean correct) {
        // Si el cardResponse es correcto se guarda y se borra la carta de la lista de las posibilidades
        CardResponseModel cardResponse = new CardResponseModel();
        int id;

        id = resultResponseMap.get(card.getId()).size() + 1;
        
        cardResponse.setCard(card);
        cardResponse.setId(id);
        cardResponse.setCorrect(correct);
        
        if(correct) {
            // ELIMINAR LA CARTA DE LA LISTA DE CARTAS PENDIENTES
        }
        resultResponseMap.get(card).add(cardResponse);
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
