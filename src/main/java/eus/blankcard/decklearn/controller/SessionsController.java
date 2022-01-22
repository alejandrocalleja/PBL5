package eus.blankcard.decklearn.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.deck.DeckRepository;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class SessionsController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    DeckRepository deckRepository;
    
    @GetMapping("/sessions")
    public String getSessions(HttpServletRequest req, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel user = userRepository.findByUsername(username);

        List<DeckModel> studySessions = new ArrayList<>();

        user.getTrainings().forEach(t -> studySessions.add(t.getDeck()));;
        req.setAttribute("decks", studySessions);

        req.setAttribute("pageName", "Study sessions");
        req.setAttribute("sessions", true);
        
        return "search_results";
    }

}
