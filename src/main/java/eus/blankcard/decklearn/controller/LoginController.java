package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/login")
    public String userLogin(HttpServletRequest req, HttpServletResponse response) {
        // HttpSession session = req.getSession();

        // String redirectPath = "";

        // String username = req.getParameter("username");
        // String password = req.getParameter("password");

        // DeckUser user = null;

        // user = userRepository.loadUser(username, password);

        // if(user != null) {
        //     session.setAttribute("user", user);
        //     redirectPath = "redirect:/home";
        // } else {
        //     session.removeAttribute("user");
        //     session.setAttribute("username", username);
        //     session.setAttribute("error", "Error in login");
        //     redirectPath = "redirect:/login";
        // }

        // return redirectPath;
        System.out.println("Post login");
        return "";
    }
}