package eus.blankcard.decklearn.controller;

import java.sql.Time;
import java.text.SimpleDateFormat;
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
import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.models.card.CardModel;
import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
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
    private String studyDeck(@PathVariable("deckId") Integer deckId,  HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        UserModel user = userRepository.findByUsername(loggedUser);
        DeckModel deck = deckRepository.getById(deckId);


        // Try loading the trainign and if null (not exist) create one.
        TrainingModel training = trainingRepository.findByUserIdInAndDeckId(user.getId(), deckId);
        if (training == null) {
            training = new TrainingModel();
            training.setUser(user);
            training.setDeck(deck);
            training.setTrainingDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            training = trainingRepository.save(training);
        }
        
        // Create a new Training Session and save it on the database
        TrainingSessionModel trainingSession = new TrainingSessionModel();
        trainingSession.setTraining(training);
        trainingSession.setDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        trainingSession = trainingSessionRepository.save(trainingSession);


        // Add the current session to the sessionManager
        sessionManager.addSession(loggedUser, trainingSession);

        return "redirect:/study/" + deckId;
    }

    @GetMapping("/study/{deckId}")
    private String getStudyView(@PathVariable("deckId") Integer deckId, HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        DeckModel deck = deckRepository.getById(deckId);

        SessionCardManager sessionCardManager = sessionManager.getSession(loggedUser);

        // Get the card Id and load all the params from DB bc if there is a second time the card is empty
        CardModel card = sessionCardManager.getNextCard();
        Optional<CardModel> optional = cardRepository.findById(card.getId());

        
        TrainingSessionModel currentTraining = sessionCardManager.getCurrentTrainingSession();
        // Add sessionId to response header (for testing)
        response.setHeader("sessionId", String.valueOf(currentTraining.getId()));

        if(optional.isPresent()) {
            card = optional.get();

            req.setAttribute("card", card);
            req.setAttribute("deck", deck);
            req.setAttribute("sessions", true);
    
            return "study/card_question";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/study/{deckId}/{cardId}")
    private String getResponseView(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId, HttpServletRequest req, HttpServletResponse response) {
        CardModel card = cardRepository.getById(cardId);
        DeckModel deck = deckRepository.getById(deckId);

        req.setAttribute("deck", deck);
        req.setAttribute("card", card);
        req.setAttribute("sessions", true);

        return "/study/card_answer";
    }

    @PostMapping("/study/{deckId}/{cardId}")
    private String saveResponse(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId, HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        String cardResult = (String) req.getParameter("response");

        boolean correct = cardResult.equals("pass");
        CardModel card = cardRepository.getById(cardId);

        SessionCardManager sessionCardManager = sessionManager.getSession(loggedUser);
        sessionCardManager.saveCardResponse(card, correct);

        if (sessionCardManager.cardsRemaining()) {
            return "redirect:/study/" + deckId;
        } else {
            sessionCardManager.saveSessionResults();
            TrainingSessionModel currentTraining = sessionCardManager.getCurrentTrainingSession();
            sessionManager.removeSession(loggedUser);

            return "redirect:/study/stats/" + currentTraining.getId();
        }
    }

    @GetMapping("/study/stats/{trainingSessionId}")
    private String getTrainingSessionStats(@PathVariable("trainingSessionId") Integer trainingSessionId, HttpServletRequest req, HttpServletResponse response) {

        Optional<TrainingSessionModel> optionalTrainingSession = trainingSessionRepository.findById(trainingSessionId);

        if(optionalTrainingSession.isPresent()) {
            TrainingSessionModel trainingSession = optionalTrainingSession.get();
            DeckModel deck = trainingSession.getTraining().getDeck();
    
            Time totalStudyTime = statsCalculator.getTotalStudyTime(trainingSession);
            String studyTimeFormat = new SimpleDateFormat("mm:ss").format(totalStudyTime);

            Time avgResponseTime = statsCalculator.getAvgResponseTime(trainingSession);
            String avgFormat = new SimpleDateFormat("mm:ss").format(avgResponseTime);
            
            int totalSessions = trainingSession.getTraining().getTrainingSessions().size();
            int passRatio = statsCalculator.getPassRatio(trainingSession);
            float gradeChange = statsCalculator.getGradeChange(trainingSession, passRatio);
    
            req.setAttribute("deck", deck);
            req.setAttribute("totalStudyTime", studyTimeFormat);
            req.setAttribute("avgResponseTime", avgFormat);
            req.setAttribute("totalSessions", totalSessions);
            req.setAttribute("passRatio", passRatio);
            req.setAttribute("gradeChange", gradeChange);
            req.setAttribute("stats", true);
    
            req.setAttribute("up", gradeChange >= 0 ? true : false);
            req.setAttribute("down", gradeChange < 0 ? true : false);
            
            return "study/session_review";
        } else {
            return "redirect:/error";
        }
    }
}