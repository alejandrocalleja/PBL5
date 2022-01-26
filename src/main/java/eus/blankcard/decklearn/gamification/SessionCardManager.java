package eus.blankcard.decklearn.gamification;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

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

import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.models.card.CardModel;
import eus.blankcard.decklearn.models.card.CardResponseModel;
import eus.blankcard.decklearn.repository.CardRepository;
import eus.blankcard.decklearn.repository.CardResponseRepository;
import eus.blankcard.decklearn.repository.ResultsRepository;
import eus.blankcard.decklearn.repository.trainingsession.TrainingSessionRepository;

@Component
@Scope("prototype")
public class SessionCardManager {
  private TrainingSessionModel trainingSession;
  private List<CardModel> cards;
  private Map<Integer, List<CardResponseModel>> resultResponseMap;
  private LocalDateTime cardSendTime;

  @Autowired
  CardResponseRepository cardResponseRepository;

  @Autowired
  TrainingSessionRepository trainingSessionRepository;

  @Autowired
  CardRepository cardRepository;

  @Autowired
  ResultsRepository resultsRepository;

  public SessionCardManager() {
    resultResponseMap = new HashMap<>();
    cards = new ArrayList<>();
  }

  public void initSessionCardManager(TrainingSessionModel trainingSession) {
    this.trainingSession = trainingSession;

    loadCards();
    initMap();
  }

  private void initMap() {
    cards.forEach(c -> resultResponseMap.put(c.getId(), new ArrayList<>()));
  }

  private void loadCards() {
    List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();

    // If it's the first traininSession of the training load all the cards

    if (traininSessions.size() <= 1) {
      this.cards = trainingSession.getTraining().getDeck().getCards();
    } else {
      // Get the previous to the current one
      TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);

      LocalDateTime prevDate = prevTraining.getDate().toLocalDateTime();
      Long daysBetween = Duration.between(prevDate, LocalDateTime.now()).toDays();

      // If it's the second study today, load all the cards and ignore the previous
      if (daysBetween < 1) {
        this.cards = trainingSession.getTraining().getDeck().getCards();

      } else {

        loadRequiredCards();
      }
    }

  }

  private void loadRequiredCards() {
    // Load the previous
    List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();
    TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);

    if (prevTraining != null) {

      prevTraining.getResults().forEach(result -> {
        int boxNum = result.getBoxNumber();
        LocalDate resDate = result.getCardResponses().get(0).getResponseDate().toLocalDate();

        if (boxNum != 0) {
          Long daysBetween = Duration.between(resDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
          CardModel card = result.getCardResponses().get(0).getCard();

          if (Math.pow(2, boxNum - (double) 1) <= daysBetween) {
            cards.add(card);
          } else {
            // If the card is not required I pass the previous cardResponses
            resultResponseMap.put(card.getId(), result.getCardResponses());
          }
        }
      });
    } else {
      this.cards = trainingSession.getTraining().getDeck().getCards();
    }
  }

  public void saveSessionResults() {
    List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();

    resultResponseMap.forEach((k, v) -> {

      ResultsModel result = new ResultsModel();
      CardModel card = cardRepository.getById(k);

      result.setCard(card);
      result.setTrainingSession(trainingSession);

      // Mirar el anterior que caja es. Si no existe anterior a la caja 1, si existe y
      // es a la primera bien
      // Anterior caja + 1 y si está mal directamente a la 1.
      if (v.size() > 1) {
        result.setBoxNumber(1);

      } else {
        // If it's the first trainingSession and the card is correct put on box 1
        if (traininSessions.size() <= 1) {

          result.setBoxNumber(1);
        } else {
          TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);

          // I load the whole prevTraining to be able to acces it's child tables. Bc if i
          // dont load it, i get a LazyInitializationException
          prevTraining = trainingSessionRepository.getById(prevTraining.getId());

          List<ResultsModel> previousResults = prevTraining.getResults();

          for (ResultsModel prevResult : previousResults) {
            if (prevResult.getCard().getId().equals(k)) {
              result.setBoxNumber(prevResult.getBoxNumber() + 1);
              break;
            }
          }
        }
      }

      result.setErrorCount(v.size());
      result.setAvgTime(calculateAvgTime(v));

      result = resultsRepository.save(result);

      // Ponerle el padre a cada CardResponse y guardarla
      for (CardResponseModel cardResponse : v) {
        cardResponse.setResult(result);
        cardResponseRepository.save(cardResponse);
      }
    });
  }

  /**
   * Calculates the average time of the given List of CardResponseModel.
   *
   * @param cardResponses List of CardResponseModel.
   * @return Time with the average time of the given List of CardResponseModel.
   */
  private Time calculateAvgTime(List<CardResponseModel> cardResponses) {
    long totalMilis = 0;

    for (CardResponseModel cardResponse : cardResponses) {
      LocalTime resTim = cardResponse.getResponseTime().toLocalTime();
      LocalDateTime responseTime = LocalDateTime.of(LocalDate.now(), resTim);

      long sec = responseTime.getSecond();
      long min = responseTime.getMinute();

      totalMilis += sec * 1000;
      totalMilis += (min * 60) * 1000;
    }

    long milisMean = totalMilis / cardResponses.size();

    return new Time(milisMean);
  }

  public TrainingSessionModel getTrainingSessionId() {
    return trainingSession;
  }

  public void setTrainingSessionId(TrainingSessionModel trainingSession) {
    this.trainingSession = trainingSession;
  }

  /**
   * Saves a card response into database.
   * The card must have been loaded from the database
   * This method will add the card to the current session buffer if it's not
   * correct.
   *
   * @param card    The card that has been answered by the user
   * @param correct The boolean that indicates if the card was answered correctly
   * @return void
   */
  public void saveCardResponse(CardModel card, boolean correct) {
    LocalDateTime cardReciveTime = LocalDateTime.now();

    Time responseTime = calculateTimeDifference(cardReciveTime, cardSendTime);

    // Si el cardResponse es correcto se guarda y se borra la carta de la lista de
    // las posibilidades
    CardResponseModel cardResponse = new CardResponseModel();

    cardResponse.setCard(card);
    cardResponse.setCorrect(correct);
    cardResponse.setResponseTime(responseTime);
    cardResponse.setResponseDate(java.sql.Date.valueOf(LocalDate.now()));

    // Si está mal la vuelves a meter para que la pregunte otra vez
    if (!correct) {
      cards.add(card);
    }
    resultResponseMap.get(card.getId()).add(cardResponse);
  }

  /**
   * Returns the next card of the study session and deletes it.
   * When this method is called, the current time is stored as the time when the
   * card was sent.
   * This method will return null if there are no more cards to study.
   *
   * @param card    The card that has been answered by the user
   * @param correct The boolean that indicates if the card was answered correctly
   * @return CardModel if the card was found
   *         of null if there are no more cards to study.
   */
  public CardModel getNextCard() {
    CardModel card = null;

    if (!cards.isEmpty()) {
      card = cards.get(0);
      cards.remove(card);
    }

    cardSendTime = LocalDateTime.now();

    return card;
  }

  public boolean cardsRemaining() {
    return !cards.isEmpty();
  }

  /**
   * Returns the difference between two LocalDateTimes.
   *
   * @param card    The card that has been answered by the user
   * @param correct The boolean that indicates if the card was answered correctly
   * @return CardModel if the card was found
   *         of null if there are no more cards to study.
   */
  private Time calculateTimeDifference(LocalDateTime closestTime, LocalDateTime otherTime) {
    long secDiff = SECONDS.between(otherTime, closestTime);
    long minDiff = MINUTES.between(otherTime, closestTime);

    long milis = 0;
    milis += secDiff * 1000;
    milis += (minDiff * 60) * 1000;

    // - 1h bc Time adds an hour depending on your GTM
    milis -= 3600000;

    return new Time(milis);
  }

  public TrainingSessionModel getCurrentTrainingSession() {
    return this.trainingSession;
  }
}