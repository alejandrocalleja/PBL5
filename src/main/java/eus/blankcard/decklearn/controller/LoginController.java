package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(HttpServletRequest req, HttpServletResponse response) {
        HttpSession session = req.getSession();

        String redirectPath = "";

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username);
        System.out.println(password);

        UserModel user = null;

        user = userRepository.loadUser(username, password);

        if(user != null) {
            session.setAttribute("user", user);
            redirectPath = "redirect:/home";
        } else {
            session.removeAttribute("user");
            session.setAttribute("username", username);
            session.setAttribute("error", "Error in login");
            redirectPath = "redirect:/login";
        }

        return redirectPath;
    }
}