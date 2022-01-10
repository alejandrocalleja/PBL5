package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        UserModel user = null;
        user = userRepository.findByUsername(username);
        System.out.println("GET USERNAME: " + username);

        if (user != null) {
            req.setAttribute("user", user);
            req.setAttribute("followers", 0);
            req.setAttribute("following", 0);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            System.out.println("REGISTEREED USERNAME: " + currentPrincipalName);
            if(currentPrincipalName.equals(username)) {
                return "profile";
            } else {
                return "profile_visit";
            }
        } else {
            return "error";
        }
    }

    @PostMapping("/{username}/follow")
    public String follow() {
        return "profile";
    }
}