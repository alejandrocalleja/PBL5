package eus.blankcard.decklearn.controller;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.gamification.SessionCardManager;
import eus.blankcard.decklearn.gamification.SessionManager;
import eus.blankcard.decklearn.models.CardModel;
import eus.blankcard.decklearn.models.DeckModel;
import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.CardRepository;
import eus.blankcard.decklearn.repository.TrainingRepository;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.trainingSession.TrainingSessionRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.StatsCalculator;

@Controller
public class StudyController {

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    TrainingSessionRepository trainingSessionRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    SessionManager sessionManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatsCalculator statsCalculator;

    @PostMapping("/study/{deckId}")
    private String studyDeck(@PathVariable("deckId") Integer deckId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        UserModel user = userRepository.findByUsername(loggedUser);
        DeckModel deck = deckRepository.getById(deckId);

        System.out.println("User " + loggedUser + " started a study session with deck " + deckId);

        // Try loading the trainign and if null (not exist) create one.
        TrainingModel training = trainingRepository.findByUserIdInAndDeckId(user.getId(), deckId);
        if (training == null) {
            System.out.println("There is no existing training.");
            training = new TrainingModel();
            training.setUser(user);
            training.setDeck(deck);
            training.setTrainingDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            training = trainingRepository.save(training);
            System.out.println("Creating one with id " + training.getId());
        }
        
        // Create a new Training Session and save it on the database
        TrainingSessionModel trainingSession = new TrainingSessionModel();
        trainingSession.setTraining(training);
        trainingSession.setDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        trainingSession = trainingSessionRepository.save(trainingSession);

        System.out.println("Created a new training session with id " + trainingSession.getId());

        // Add the current session to the sessionManager
        sessionManager.addSession(loggedUser, trainingSession);

        return "redirect:/study/" + deckId;
    }

    @GetMapping("/study/{deckId}")
    private String getStudyView(@PathVariable("deckId") Integer deckId, HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        DeckModel deck = deckRepository.getById(deckId);
        System.out.println("Deck loaded in getStudyView()");
        System.out.println("Deck: " + deck.getId());

        SessionCardManager sessionCardManager = sessionManager.getSession(loggedUser);
        System.out.println("Loading the sessionManager from getStudyView()");

        // Get the card Id and load all the params from DB bc if there is a second time the card is empty
        CardModel card = sessionCardManager.getNextCard();
        Optional<CardModel> optional = cardRepository.findById(card.getId());
        card = optional.get();

        req.setAttribute("card", card);
        req.setAttribute("deck", deck);

        return "study/card_question";
    }

    @GetMapping("/study/{deckId}/{cardId}")
    private String getResponseView(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId, HttpServletRequest req, HttpServletResponse response) {
        CardModel card = cardRepository.getById(cardId);
        DeckModel deck = deckRepository.getById(deckId);

        req.setAttribute("deck", deck);
        req.setAttribute("card", card);

        return "/study/card_answer";
    }

    @PostMapping("/study/{deckId}/{cardId}")
    private String saveResponse(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId, HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        String cardResult = (String) req.getParameter("response");
        System.out.println("Result: " + cardResult);

        boolean correct = cardResult.equals("pass");
        CardModel card = cardRepository.getById(cardId);

        SessionCardManager sessionCardManager = sessionManager.getSession(loggedUser);
        sessionCardManager.saveCardResponse(card, correct);

        if (sessionCardManager.cardsRemaining()) {
            System.out.println("Cards reamining -> loop study again");
            return "redirect:/study/" + deckId;
        } else {
            System.out.println("No crds reamining -> save results and go");
            sessionCardManager.saveSessionResults();
            TrainingSessionModel currentTraining = sessionCardManager.getCurrentTrainingSession();
            sessionManager.removeSession(loggedUser);

            return "redirect:/study/ststs/" + currentTraining.getId();
        }
    }

    @GetMapping("/study/ststs/{trainingSessionId}")
    private String getTrainingSessionStats(@PathVariable("trainingSessionId") Integer trainingSessionId, HttpServletRequest req, HttpServletResponse response) {

        Optional<TrainingSessionModel> optionalTrainingSession = trainingSessionRepository.findById(trainingSessionId);
        TrainingSessionModel trainingSession = optionalTrainingSession.get();
        DeckModel deck = trainingSession.getTraining().getDeck();

        Time totalStudyTime = statsCalculator.getTotalStudyTime(trainingSession);
        Time avgResponseTime = statsCalculator.getAvgResponseTime(trainingSession);
        int totalSessions = trainingSession.getTraining().getTrainingSessions().size();
        int passRatio = statsCalculator.getPassRatio(trainingSession);

        req.setAttribute("deck", deck);
        req.setAttribute("totalStudyTime", "00:00:32");
        req.setAttribute("avgResponseTime", avgResponseTime.toString());
        req.setAttribute("totalSessions", totalSessions);
        req.setAttribute("passRatio", passRatio);
        
        return "study/session_review";
    }
}