package eus.blankcard.decklearn.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

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
import eus.blankcard.decklearn.repository.TrainingSessionRepository;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;

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
        trainingSession.setId(training.getTrainingSessions().size() + 1);
        trainingSession.setDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        trainingSessionRepository.save(trainingSession);
        System.out.println("Creating a new training session with id " + trainingSession.getId());

        // Save the training/training session relation
        // training.addTrainingSession(trainingSession);
        // trainingRepository.save(training);
        // System.out.println("Training saved");

        // Add the current session to the sessionManager
        sessionManager.addSession(loggedUser, trainingSession);

        return "redirect:/study/" + deckId;
    }

    @GetMapping("/study/{deckId}")
    private String getStudyView(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {
        // Aqui vas a venir muchas veces
        // Uno de los hilos va a empezar a elegir la siguiente carta mientras que el
        // otro va a estar durmiendo esperando a que le avise oq ue haya content en el buffer
        // Una vez encontrada la carta se devuelve una vista con la pregunta.

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        DeckModel deck = deckRepository.getById(deckId);
        System.out.println("HAS CARGADO EL MAZO BROOO");
        System.out.println("deck: " + deck.getCards().get(0).getQuestion());

        SessionCardManager sessionCardManager = sessionManager.getSession(loggedUser);
        System.out.println("Loading the sessionManager from getStudyView()");

        // CardModel card = sessionCardManager.getNextCard();
        req.setAttribute("card", deck.getCards().get(0));
        req.setAttribute("deck", deck);

        return "/study/card_question";
    }

    @GetMapping("/study/{deckId}/{cardId}")
    private String getResponseView(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId,
            HttpServletRequest req,
            HttpServletResponse response) {
        // Returneas la vista con la respuesta de esa carta
        // CardModel card = cardRepository.getById(cardId);
        
        DeckModel deck = deckRepository.getById(deckId);
        req.setAttribute("deck", deck);
        req.setAttribute("card", deck.getCards().get(0));

        return "/study/card_answer";
    }

    @PostMapping("/study/{deckId}/{cardId}")
    private String saveResponse(@PathVariable("deckId") Integer deckId, @PathVariable("cardId") Integer cardId , HttpServletRequest req, 
    HttpServletResponse response) {
        // Notificas al hilo de la respuesta para que empieze a seleccionar la siguente carta
        // Aquí pillas el name del boton que te ha mandado el post. Una vez sabes si ha sido respuesta buena o mala tienes que guardarlo en response.
        // Return la vista con la siguiente carta. Si por alguna razón era la última carta y la ha respondido bien puedes pasar a la vista de stats

        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String currentPrincipalName = authentication.getName();

        // // int response = req.getAttribute("response");
        // ResultsModel results = new ResultsModel();
        // results.setTrainingSession(trainingSession);

        // CardResponseModel cardResponseModel = new CardResponseModel();
        // cardResponseModel.setCard(card);
        // cardResponseModel.setResults(results);

        // SessionCardManager sessionCardManager = sessionManager.getSession(currentPrincipalName);
        // sessionCardManager.saveCardResponse(cardResponse);

        // if() {

        // } else {
            
        // }

        // Returnear un redirect a study si todavia quedan cartas, si no tienes que hacer un redirect a trainingStats
        return "redirect:/study/" + deckId;
    }
}