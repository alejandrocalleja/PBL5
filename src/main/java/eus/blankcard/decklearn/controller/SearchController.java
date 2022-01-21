package eus.blankcard.decklearn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.DeckModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;


@Controller
public class SearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeckRepository deckRepository;

    @GetMapping(value="/deck/search")
    public String getSearchResults(HttpServletRequest req, HttpServletResponse response) {
        String title = (String) req.getParameter("searchName");

        String strippedTitle = title.strip();

        if(strippedTitle.equals("")) {
            return "redirect:/home";
        } else {
            List<DeckModel> searchResult = deckRepository.findByTitleContaining(title);
    
            req.setAttribute("decks", searchResult);
    
            req.setAttribute("pageName", "Search results");
            req.setAttribute("home", true);
    
            return "search_results";
        }

    }
    
}
