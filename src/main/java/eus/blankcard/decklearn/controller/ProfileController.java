package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

  String errorPage = "error";
  String profilePage = "profile";
  String followingPage = "following";
  String followersPage = "followers";

  @Autowired
  UserRepository userRepository;

  @GetMapping("/{username}")
  public String getProfile(@PathVariable("username") String username, HttpServletRequest req,
      HttpServletResponse response) {
    UserModel user = null;

    user = userRepository.findByUsername(username);
    if (user != null) {
      req.setAttribute("user", user);

      req.setAttribute(followersPage, user.getFollowers().size());
      req.setAttribute(followingPage, user.getFollowed().size());
      req.setAttribute("decks", true);
      req.setAttribute("userDecks", user.getDecks());

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String currentPrincipalName = authentication.getName();

      if (currentPrincipalName.equals(username)) {
        return profilePage;
      } else {
        return "profile_visit";
      }

    } else {
      response.setStatus(404);
      return errorPage;
    }
  }

  @GetMapping("/{username}/followers")
  public String getFollowers(@PathVariable("username") String username, HttpServletRequest req,
      HttpServletResponse response) {
    UserModel user = userRepository.findByUsername(username);

    req.setAttribute(followersPage, user.getFollowers());

    return followingPage;
  }

  @GetMapping("/{username}/following")
  public String getFollowing(@PathVariable("username") String username, HttpServletRequest req,
      HttpServletResponse response) {
    UserModel user = userRepository.findByUsername(username);

    req.setAttribute(followersPage, user.getFollowed());

    return followingPage;
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

      req.setAttribute(followersPage, user.getFollowers().size());
      req.setAttribute(followingPage, user.getFollowed().size());
      req.setAttribute("saved", true);
      req.setAttribute("userDecks", user.getSavedDecks());
      return profilePage;
    } else {
      return errorPage;
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

      req.setAttribute("user", user);

      req.setAttribute(followersPage, user.getFollowers().size());
      req.setAttribute(followingPage, user.getFollowed().size());
      req.setAttribute("sessions", true);
      // req.setAttribute("userDecks", user.getTrainings());
      return profilePage;
    } else {
      return errorPage;
    }
  }

  @PostMapping("/{username}/follow")
  public String follow(@PathVariable("username") String username, HttpServletRequest req,
      HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    UserModel loggedUser = userRepository.findByUsername(currentPrincipalName);
    UserModel targetUser = userRepository.findByUsername(username);

    if (userRepository.checkFollowed(loggedUser, targetUser)) {
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