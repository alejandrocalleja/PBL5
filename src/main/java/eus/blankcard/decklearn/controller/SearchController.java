package eus.blankcard.decklearn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class SearchController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  DeckRepository deckRepository;

  @GetMapping(value = "/deck/search")
  public String getSearchResults(HttpServletRequest req, HttpServletResponse response) {
    String title = req.getParameter("searchName");

    String strippedTitle = title.trim();

    if (strippedTitle.equals("")) {
      return "redirect:/home";
    } else {
      Pageable limit = PageRequest.of(0, 20);

      List<DeckModel> searchResult = deckRepository.findByTitleContaining(title, limit);

      req.setAttribute("decks", searchResult);

      req.setAttribute("pageName", "Search results");
      req.setAttribute("home", true);

      return "search_results";
    }

  }
}