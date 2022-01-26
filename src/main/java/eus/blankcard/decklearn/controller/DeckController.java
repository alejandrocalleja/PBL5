package eus.blankcard.decklearn.controller;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.DeckCreationUtils;
import eus.blankcard.decklearn.util.StatsCalculator;

@Controller
public class DeckController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    DeckCreationUtils deckCreationUtils;

    @Autowired
    StatsCalculator statsCalculator;

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

        
        AtomicInteger monthStudies = statsCalculator.getMonthStudies(deck);
        int totalStudies = deck.getTrainings().size();
        Time avgTime = statsCalculator.getAvgResponseTime(deck);
        int totalSaves = deck.getSavers().size();
        int averagePass = statsCalculator.getAveragePassRatio(deck);
        
        req.setAttribute("deck", deck);
        req.setAttribute("monthStudies", monthStudies);
        req.setAttribute("totalStudies", totalStudies);
        req.setAttribute("avgTime", avgTime);
        req.setAttribute("totalSaves", totalSaves);
        req.setAttribute("averagePass", averagePass);
        req.setAttribute("stats", true);

        return "/deck/deck_stats";
    }

    @PostMapping("/deck/{deckId}/save")
    public String saveDeck(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {

        Optional<DeckModel> optionalDeck = deckRepository.findById(deckId);

        if (optionalDeck.isPresent()) {
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

    @PostMapping("/deck/{deckId}/remove")
    public String deleteDeck(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {

        Optional<DeckModel> optionalDeck = deckRepository.findById(deckId);

        if (optionalDeck.isPresent()) {
            DeckModel deck = optionalDeck.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedUsername = authentication.getName();

            UserModel loggedUser = userRepository.findByUsername(loggedUsername);

            if (deck.getCreator().getId().equals(loggedUser.getId())) {
                deckRepository.delete(deck);
            } else {
                return "redirect:/error";
            }

            return "redirect:/" + loggedUsername;

        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/create/deck")
    public String getCreationForm(HttpServletRequest req,
            HttpServletResponse response) {

        DeckModel deck = new DeckModel();
        deck.setCards(new ArrayList<>());

        req.setAttribute("deck", deck);
        req.setAttribute("cardNum", deck.getCards().size());
        req.setAttribute("action", "new");
        req.setAttribute("create", true);

        return "deck/deck_creation";
    }

    @GetMapping("/create/deck/{deckId}")
    public String getCreateFormWithDeck(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse res) {

        DeckModel deck = deckRepository.getById(deckId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        UserModel userModel = userRepository.findByUsername(loggedUsername);

        if (deck.getCreator().getId().equals(userModel.getId())) {
            req.setAttribute("deck", deck);
            req.setAttribute("cardNum", deck.getCards().size());
            req.setAttribute("action", "edit");
            req.setAttribute("create", true);
            return "deck/deck_creation";
        } else {
            return "redirect:/error";
        }

    }

    @PostMapping("/create/deck")
    public String saveDeckForFirstTime(HttpServletRequest req, HttpServletResponse res) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        UserModel loggedUser = userRepository.findByUsername(loggedUsername);

        DeckModel deck = new DeckModel();

        deck.setCreator(loggedUser);
        deck = deckRepository.save(deck);

        deck.setTitle(req.getParameter("title"));
        deck.setDescription(req.getParameter("description"));
        deck.setImgPath("/images/deck/default.png");

        String redirectUrl = "redirect:/create/deck/" + deck.getId();
        String action = req.getParameter("action");

        redirectUrl = deckCreationUtils.checkAction(req, res, deck, action);

        return redirectUrl;
    }

    @PostMapping("/create/deck/{deckId}")
    public String createDeck(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse res) {
            
        DeckModel deck = deckRepository.getById(deckId);            
        deck.setTitle(req.getParameter("title"));
        deck.setDescription(req.getParameter("description"));
        deck.setImgPath("/images/deck/default.png");

        
        String redirectUrl = "redirect:/create/deck/" + deck.getId();
        String action = req.getParameter("action");

        redirectUrl = deckCreationUtils.checkAction(req, res, deck, action);

        return redirectUrl;
    }
}
