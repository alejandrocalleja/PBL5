package eus.blankcard.decklearn.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.DeckModel;
import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class DeckController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeckRepository deckRepository;

    @GetMapping("/deck/{deckId}")
    public String getMethodName(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        UserModel loggedUser = userRepository.findByUsername(loggedUsername);

        DeckModel deck = deckRepository.getById(deckId);
        UserModel creator = deck.getCreator();

        req.setAttribute("deck", deck);
        req.setAttribute("cardNumber", deck.getCards().size());
        req.setAttribute("types", deck.getTypes());
        req.setAttribute("creator", creator.getUsername());
        req.setAttribute("studies", deck.getTrainings().size());
        req.setAttribute("home", true);
        req.setAttribute("saved", loggedUser.getSavedDecks().contains(deck));
        req.setAttribute("isCreator", loggedUser.getDecks().contains(deck));

        return "/deck/deck_view";
    }

    @GetMapping("/deck/{deckId}/stats")
    public String getDeckStats(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {
        DeckModel deck = deckRepository.getById(deckId);

        req.setAttribute("deck", deck);

        LocalDate now = LocalDate.now();
        List<TrainingModel> monthTraining = deck.getTrainings().stream()
                .filter(t -> t.getTrainingDate().toLocalDateTime().getMonthValue() == now.getMonthValue())
                .collect(Collectors.toCollection(ArrayList::new));

        req.setAttribute("monthStudies", monthTraining.size());
        req.setAttribute("totalStudies", deck.getTrainings().size());
        req.setAttribute("avgTime", 1);
        req.setAttribute("totalSaves", deck.getSavers().size());
        req.setAttribute("averagePass", 100);
        req.setAttribute("stats", true);

        return "/deck/deck_stats";
    }

    @PostMapping("/deck/{deckId}/save")
    public String saveDeck(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {

        Optional<DeckModel> optionalDeck = deckRepository.findById(deckId);

        if(optionalDeck.isPresent()) {
            DeckModel deckModel = optionalDeck.get();
    
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedUsername = authentication.getName();
    
            UserModel userModel = userRepository.findByUsername(loggedUsername);
    
            if (userModel.getSavedDecks().contains(deckModel)) {
                System.out.println("The user has it saved. Unsaving it");
                userModel.getSavedDecks().remove(deckModel);
            } else {
                userModel.getSavedDecks().add(deckModel);
            }
    
            userRepository.save(userModel);
    
            return "redirect:/deck/" + deckModel.getId();
        } else {
            return "redirect:/error";
        }
        
    }
}
