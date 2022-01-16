package eus.blankcard.decklearn.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
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
        req.setAttribute("studies", deck.getTrainings().size());

        return "/deck/deck_view";
    }

    @GetMapping("/deck/{deckId}/stats")
    public String getDeckStats(@PathVariable("deckId") Integer deckId, HttpServletRequest req,
            HttpServletResponse response) {
        DeckModel deck = deckRepository.getById(deckId);

        req.setAttribute("deck", deck);

        LocalDate now = LocalDate.now();
        List<TrainingModel> monthTraining = deck.getTrainings().stream()
                .filter(t -> t.getTraining_date().toLocalDate().getMonthValue() == now.getMonthValue())
                .collect(Collectors.toCollection(ArrayList::new));

        req.setAttribute("monthStudies", monthTraining.size());
        req.setAttribute("totalStudies", deck.getTrainings().size());
        req.setAttribute("avgTime", 1);
        req.setAttribute("totalSaves", deck.getSavers().size());
        req.setAttribute("averagePass", 100);

        return "deck/deck_stats";
    }
}
