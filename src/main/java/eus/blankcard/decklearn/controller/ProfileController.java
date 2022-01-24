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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.UserUtils;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtils userUtils;

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        UserModel user = null;

        user = userRepository.findByUsername(username);
        if (user != null) {
            req.setAttribute("user", user);

            req.setAttribute("followers", user.getFollowers().size());
            req.setAttribute("following", user.getFollowed().size());            
            req.setAttribute("profile", true);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String loggedUser = authentication.getName();


            if (loggedUser.equals(username)) {
                req.setAttribute("userDecks", user.getDecks());
                req.setAttribute("decks", true);
                return "user/profile";
            } else {

                return "user/profile_visit";
            }

        } else {
            response.setStatus(404);
            return "error";
        }
    }

    @GetMapping("/{username}/followers")
    public String getFollowers(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        UserModel user = userRepository.findByUsername(username);

        req.setAttribute("followers", user.getFollowers());
        req.setAttribute("profile", true);

        return "user/following";
    }

    @GetMapping("/{username}/following")
    public String getFollowing(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        UserModel user = userRepository.findByUsername(username);

        req.setAttribute("followers", user.getFollowed());
        req.setAttribute("profile", true);

        return "user/following";
    }

    @GetMapping("/{username}/saved")
    public String getSaved(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (username.equals(currentPrincipalName)) {
            UserModel user = null;
            user = userRepository.findByUsername(username);

            req.setAttribute("user", user);

            req.setAttribute("followers", user.getFollowers().size());
            req.setAttribute("following", user.getFollowed().size());
            req.setAttribute("saved", true);
            req.setAttribute("userDecks", user.getSavedDecks());
            return "user/profile";
        } else {
            return "error";
        }
    }

    @GetMapping("/{username}/sessions")
    public String getSessions(@PathVariable("username") String username, HttpServletRequest req,
            HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (username.equals(currentPrincipalName)) {
            UserModel user = null;
            user = userRepository.findByUsername(username);

            List<DeckModel> trainingSessions = new ArrayList<>();
            user.getTrainings().forEach(t -> trainingSessions.add(t.getDeck()));

            req.setAttribute("user", user);

            req.setAttribute("followers", user.getFollowers().size());
            req.setAttribute("following", user.getFollowed().size());
            req.setAttribute("sessions", true);
            req.setAttribute("userDecks", trainingSessions);
            return "user/profile";
        } else {
            return "error";
        }
    }

    @PostMapping("/{username}/follow")
    public String follow(@PathVariable("username") String username, HttpServletRequest req, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        UserModel loggedUser = userRepository.findByUsername(currentPrincipalName);
        UserModel targetUser = userRepository.findByUsername(username);

        
        if(userUtils.checkFollowed(loggedUser, targetUser) == true) {
            loggedUser.removeFollowed(targetUser);
            targetUser.removeFollower(loggedUser);

            userRepository.save(loggedUser);
            userRepository.save(targetUser);
        } else {
            loggedUser.addFollowed(targetUser);
            targetUser.addFollower(loggedUser);

            userRepository.save(loggedUser);
            userRepository.save(targetUser);
        }

        return "redirect:/" + username;
    }
}