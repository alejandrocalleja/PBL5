package eus.blankcard.decklearn.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.UserUtils;

@Controller
public class HomeController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  DeckRepository deckRepository;

  @Autowired
  UserUtils userUtils;

  @GetMapping("/home")
  public String getHome(HttpServletRequest req, HttpServletResponse response) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    UserModel user = userRepository.findByUsername(username);

    List<DeckModel> studySessions = new ArrayList<>();

    user.getTrainings().forEach(t -> studySessions.add(t.getDeck()));
    req.setAttribute("study_sessions", studySessions);

    Set<DeckModel> followedDecks = userUtils.getFollowingDecks(user);

    if(followedDecks.size() == 0) {
      Pageable limit = PageRequest.of(0, 20);
      followedDecks = deckRepository.findAll(limit).toSet();
      req.setAttribute("listName", "Explore Decks");
    } else {
      req.setAttribute("listName", "Followed Users Decks");
    }

    req.setAttribute("explore_deck", followedDecks);

    req.setAttribute("home", true);

    return "home";
  }
}