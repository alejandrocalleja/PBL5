package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import eus.blankcard.decklearn.models.DeckModel;
import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;


@Controller
public class DeckController {

    @Autowired
    DeckRepository deckRepository;

    @GetMapping("/deck/{deckId}")
    public String getMethodName(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
    HttpServletResponse response) {
        DeckModel deck = deckRepository.getById(deckId);
        UserModel creator = deck.getCreator();

        req.setAttribute("deck", deck);
        req.setAttribute("cardNumber", deck.getCards().size());
        req.setAttribute("types", deck.getTypes());
        req.setAttribute("creator", creator.getUsername());
        req.setAttribute("studies", 2);

        return "/deck/deck_view";
    }


    @GetMapping("/deck/{deckId}/stats")
    public String getDeckStats(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
    HttpServletResponse response) {
        DeckModel deck = deckRepository.getById(deckId);
        // List<CardModel> cards = deck.getCards();

        // cards.forEach(card -> {
            
        // });
        req.setAttribute("deck", deck);
        req.setAttribute("monthStudies", 1);
        req.setAttribute("totalStudies", 1);
        req.setAttribute("avgTime", 1);
        req.setAttribute("totalSaves", 1);
        req.setAttribute("averagePass", 100);

        return "deck/deck_stats";
    }
    
}
