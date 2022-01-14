package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class HomeController {
    
    @Autowired
    UserRepository userRepository;

    @GetMapping("/home")
    public String getHome(HttpServletRequest req, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel user = userRepository.findByUsername(username);
        // req.setAttribute("study_sessions", user.getDecks());
        // req.setAttribute("explore_deck", user.getDecks());

        return "home";
    }
}
