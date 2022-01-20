package eus.blankcard.decklearn.gamification;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.HOURS;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.CardModel;
import eus.blankcard.decklearn.models.CardResponseModel;
import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.repository.CardRepository;
import eus.blankcard.decklearn.repository.CardResponseRepository;
import eus.blankcard.decklearn.repository.ResultsRepository;
import eus.blankcard.decklearn.repository.trainingSession.TrainingSessionRepository;

@Component
@Scope("prototype")
public class SessionCardManager {
    private TrainingSessionModel trainingSession;
    private Buffer buffer;
    private List<CardModel> cards;
    private Map<Integer, List<CardResponseModel>> resultResponseMap;
    private LocalTime cardSendTime;

    @Autowired
    CardResponseRepository cardResponseRepository;

    @Autowired
    TrainingSessionRepository trainingSessionRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ResultsRepository resultsRepository;

    public SessionCardManager() {
    }

    public void initSessionCardManager(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;

        System.out.println("Creating a new SessionCardManager for trainingSession " + trainingSession.getId());

        resultResponseMap = new HashMap<>();
        buffer = new Buffer();

        loadCards();
        initMap();
    }
    
    private void initMap() {
        cards.forEach(c -> resultResponseMap.put(c.getId(), new ArrayList<>()));
    }

    private void loadCards() {
        List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();

        // If it's the first traininSession of the training load all the cards
        System.out.println("There are a total of " + traininSessions.size() + " sessions.");
        if (traininSessions.size() <= 1) {
            System.out.println("It's the first study session for this training");
            System.out.println("Loading all the cards of deck");
            this.cards = trainingSession.getTraining().getDeck().getCards();
        } else {
            // Get the previous to the current one
            TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);

            LocalDateTime prevDate = prevTraining.getDate().toLocalDateTime();
            Long daysBetween = Duration.between(prevDate, LocalDateTime.now()).toDays();

            // If it's the second study today, load all the cards and ignore the previous
            System.out.println("Days between prev and current training session: " + daysBetween);
            if (daysBetween < 1) {
                System.out.println("Loading all the cards of deck");
                this.cards = trainingSession.getTraining().getDeck().getCards();
            } else {
                System.out.println("Loading required cards of the deck");
                loadRequiredCards();
            }
        }

        System.out.println(cards.size() + " cards loaded for the session.");
    }

    private void loadRequiredCards() {
        // TrainingSessionModel prevTraining = trainingSessionRepository
        //         .findLastSession(trainingSession.getTraining().getId());

        // Load the previous
        List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();
        TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);

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
                        // If the card is not required I pass the previous cardResponses
                        resultResponseMap.put(card.getId(), result.getCardResponses());
                    }
                }
            });
        } else {
            System.out.println("No previous training session. Loading all the cards");
            this.cards = trainingSession.getTraining().getDeck().getCards();
        }
    }

    public void saveSessionResults() {

        System.out.println("Saving results for training Session " + trainingSession.getId());
        List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();
        
        System.out.println("Iterating through resultsResponseMap");
        resultResponseMap.forEach((k, v) -> {
            System.out.println("Key: " + k);
            System.out.println("Value lenghth: " + v.size());

            ResultsModel result = new ResultsModel();
            CardModel card = cardRepository.getById(k);
            System.out.println("Card " + card.getId() + " loaded for the new result entry.");

            result.setCard(card);
            result.setTrainingSession(trainingSession);
            
            // Mirar el anterior que caja es. Si no existe anterior a la caja 1, si existe y es a la primera bien
            // Anterior caja + 1 y si está mal directamente a la 1.
            if (v.size() > 1) {
                System.out.println("There is more than one card response. Setting the box number to 1.");
                result.setBoxNumber(1);
            } else {
                // If it's the first trainingSession and the card is correct put on box 1
                if (traininSessions.size() <= 1)  {
                    System.out.println("It's the first training session. Setting the box number to 1");
                    result.setBoxNumber(1);
                } else {
                    System.out.println("Not the first training session. Getting the previous one.");
                    TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);
                    System.out.println("Previous training has id " + prevTraining.getId());
                    // I load the whole prevTraining to be able to acces it's child tables. Bc if i dont load it, i get a LazyInitializationException
                    prevTraining = trainingSessionRepository.getById(prevTraining.getId());
                    List<ResultsModel> previousResults = prevTraining.getResults();
                    System.out.println("Previous results size " + previousResults.size());
                    for(ResultsModel prevResult : previousResults) {
                        if(prevResult.getCard().getId() == k) {
                            System.out.println("Prev result box found. Box number: " + prevResult.getBoxNumber());
                            result.setBoxNumber(prevResult.getBoxNumber() + 1);
                            break;
                        }
                    }
                }
            }

            System.out.println("Setting error count to " + v.size());
            result.setErrorCount(v.size());
            System.out.println("Setting avg Time");
            result.setAvgTime(calculateAvgTime(v));
            
            System.out.println("Saving the result to the result repository");
            result = resultsRepository.save(result);

            System.out.println("Setting the resultModel to the cardResponses and saving them into the DB");
            // Ponerle el padre a cada CardResponse y guardarla
            for(CardResponseModel cardResponse : v) {
                cardResponse.setResult(result);
                cardResponseRepository.save(cardResponse);
            }
        });
    }

    private Time calculateAvgTime(List<CardResponseModel> v) {
        // Todo a segundos, sacar AVG de eso y pasarlo otra vez a formato

        long totalMilis = 0;

        for(CardResponseModel cardResponse : v) {
            LocalTime responseTime = cardResponse.getResponseTime().toLocalTime();

            long sec = responseTime.getSecond();
            long min = responseTime.getMinute();
            long hour = responseTime.getHour();

            totalMilis += sec * 1000;
            totalMilis += (min * 60) * 1000;
            totalMilis += ((hour * 60) * 60) * 1000;
        }

        long milisMean = totalMilis / v.size();

        Time time = new Time(milisMean);

        System.out.println("Avg time is " + time.toString());

        return time;
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
        LocalTime cardReciveTime = LocalTime.now();

        Time responseTime = calculateTimeDifference(cardReciveTime, cardSendTime);

        // Si el cardResponse es correcto se guarda y se borra la carta de la lista de las posibilidades
        CardResponseModel cardResponse = new CardResponseModel();

        cardResponse.setCard(card);
        cardResponse.setCorrect(correct);
        cardResponse.setResponseTime(responseTime);
        cardResponse.setResponseDate(java.sql.Date.valueOf(LocalDate.now()));

        // Si está mal la vuelves a meter para que la pregunte otra vez
        if (!correct) {
            System.out.println("Card not correct. Adding it again");
            cards.add(card);
            System.out.println("Cards remaining after adding: " + cards.size());
        }
        resultResponseMap.get(card.getId()).add(cardResponse);
    }

    public CardModel getNextCard() {
        CardModel card = null;

        // AQUI SE CAMBIA ESTO POR LA GAMIFICATION
        if (cards.size() > 0) {
            card = cards.get(0);
            cards.remove(card);
        }
        System.out.println("Next card is card " + card.getId());
        System.out.println("Remaining cards: " + cards.size());

        cardSendTime = LocalTime.now();

        return card;
    }

    public boolean cardsRemaining() {
        return cards.size() > 0;
    }

    private Time calculateTimeDifference(LocalTime closestTime, LocalTime otherTime) {
        
        long secDiff = SECONDS.between(closestTime, otherTime);
        long minDiff = MINUTES.between(closestTime, otherTime);
        long hourDiff = HOURS.between(closestTime, otherTime);

        long milis = 0; 
        milis += secDiff * 1000;
        milis += (minDiff * 60) * 1000;
        milis += ((hourDiff * 60) * 60) * 1000;

        Time time = new Time(milis);

        return time;
    }

}
