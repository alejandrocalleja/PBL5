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

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class ProfileController {

  String errorPage = "error";
  String profilePage = "profile";
  String followingPage = "following";
  String followersPage = "followers";

        UserModel user = null;



 

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
        return "user/following";
    }


  @GetMapping("/{username}/following")
  public String getFollowing(@PathVariable("username") String username, HttpServletRequest req,
      HttpServletResponse response) {
    UserModel user = userRepository.findByUsername(username);
    req.setAttribute(followersPage, user.getFollowed());


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
            req.setAttribute("followers", user.getFollowers().size());
            req.setAttribute("following", user.getFollowed().size());
            req.setAttribute("sessions", true);
            // req.setAttribute("userDecks", user.getTrainings());
            return "user/profile";
        } else {
            return "error";
        }
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