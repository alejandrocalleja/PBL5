package eus.blankcard.decklearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/home")
    public String getHome() {

        // Cargar los decks y ponerlos 

        return "home";
    }
}
