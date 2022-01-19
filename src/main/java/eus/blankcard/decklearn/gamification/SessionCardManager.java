package eus.blankcard.decklearn.gamification;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
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
import eus.blankcard.decklearn.repository.CardRepository;
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
    CardRepository cardRepository;

    @Autowired
    ResultsRepository resultsRepository;

    public SessionCardManager(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;

        System.out.println("Creating a new SessionCardManager for trainingSession " + trainingSession.getId());

        buffer = new Buffer();
        index = 0;
        initMap();
        loadCards();
    }

    private void initMap() {
        resultResponseMap = new HashMap<>();
        cards.forEach(c -> resultResponseMap.put(c.getId(), new ArrayList<>()));
    }

    private void loadCards() {
        // this.cards = trainingSession.getTraining().getDeck().getCards();
        this.cards = loadRequiredCards();
        System.out.println(cards.size() + " cards loaded for the session.");
    }

    private List<CardModel> loadRequiredCards() {

        // Load the previous
        TrainingSessionModel prevTraining = trainingSessionRepository
                .findByTrainingIdAndDateWithLimit(trainingSession.getTraining().getId(), 1);

        if (prevTraining != null) {
            System.out.println("Previous training found. Loading required cards.");
            
            prevTraining.getResults().forEach(result -> {
                int boxNum = result.getBoxNumber();
                LocalDate resDate = result.getCardResponses().get(0).getResponseDate().toLocalDate();

                if (boxNum != 0) {
                    Long daysBetween = Duration.between(resDate, LocalDate.now()).toDays();
                    CardModel card = result.getCardResponses().get(0).getCard();

                    if (Math.pow(2, boxNum - 1) >= daysBetween) {
                        cards.add(card);
                    } else {
                        // If the card is not required I pass the previous cardResponse
                        resultResponseMap.put(card.getId(), result.getCardResponses());
                    }
                }
            });
        } else {
            System.out.println("No previous training session. Loading all the cards");
            this.cards = trainingSession.getTraining().getDeck().getCards();
        }

        return null;
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
            // Mirar el anterior que caja es. Si no existe anterior a la caja 1, si existe y
            // es a la primera bien
            // Anterior caja + 1 y si está mal directamente a la 1.
            if (v.size() > 1) {
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
        // Si el cardResponse es correcto se guarda y se borra la carta de la lista de
        // las posibilidades
        CardResponseModel cardResponse = new CardResponseModel();
        int id;

        id = resultResponseMap.get(card.getId()).size() + 1;

        cardResponse.setCard(card);
        cardResponse.setId(id);
        cardResponse.setCorrect(correct);
        cardResponse.setResponseDate(java.sql.Date.valueOf(LocalDate.now()));

        if (!correct) {
            // Si está mal la vuelves a meter para que la pregunte otra vez
            cards.add(card);
        }
        resultResponseMap.get(card.getId()).add(cardResponse);
    }

    public CardModel getNextCard() {
        CardModel card = null;

        // AQUI SE CAMBIA ESTO POR LA GAMIFICATION
        if (index > cards.size()) {
            card = cards.get(index);
            cards.remove(card);
        }

        return card;
    }

    @Override
    public void run() {
        // cards.forEach(c -> {
        // try {
        // buffer.putValue(c);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // });
    }
}
