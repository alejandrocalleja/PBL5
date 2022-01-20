package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class SettingsController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder encoder;

  String defaultFolder = "settings/";

  @GetMapping({ "/settings/profile", "/settings" })
  public String getProfile(HttpServletRequest req, HttpServletResponse response) {
    UserModel user = null;
    String username = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    username = authentication.getName();

    user = userRepository.findByUsername(username);
    if (user != null) {
      req.setAttribute("user", user);
      return defaultFolder + "profile_settings";
    } else {
      response.setStatus(404);
      return "error";
    }
  }

  @GetMapping("/settings/security")
  public String getSecurity(HttpServletRequest req, HttpServletResponse response) {
    UserModel user = null;
    String username = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    username = authentication.getName();

    user = userRepository.findByUsername(username);
    if (user != null) {
      req.setAttribute("user", user);
      return defaultFolder + "security_settings";
    } else {
      response.setStatus(404);
      return "error";
    }
  }

  @GetMapping("/settings/language")
  public String getLanguage(HttpServletRequest req, HttpServletResponse response) {
    UserModel user = null;
    String username = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    username = authentication.getName();

    user = userRepository.findByUsername(username);
    if (user != null) {
      req.setAttribute("user", user);
      return defaultFolder + "language_settings";
    } else {
      response.setStatus(404);
      return "error";
    }
  }

  @PostMapping("/settings/profile")
  public String profileSubmit(UserModel newUser, HttpServletRequest req, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    UserModel user = userRepository.findByUsername(username);

    System.out.println(user.getUsername());

    String newName = newUser.getName();
    String newSurname = newUser.getSurname();
    String newUsername = newUser.getUsername();
    String newPostalCode = newUser.getPostalCode();
    String newCountry = newUser.getCountry();

    user.setName(newName);
    user.setSurname(newSurname);
    user.setUsername(newUsername);
    user.setPostalCode(newPostalCode);
    user.setCountry(newCountry);

    Authentication token = new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword(),
        authentication.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(token);

    userRepository.save(user);
    return "redirect:/home";
  }

  @PostMapping("/settings/security")
  public String securitySubmit(UserModel newUser, HttpServletRequest req, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    UserModel user = userRepository.findByUsername(username);

    System.out.println(user.getUsername());

    String newEmail = newUser.getEmail();
    String newPass = newUser.getPassword();

    if (newEmail != null) {
      user.setEmail(newEmail);
    } else {
      return "redirect:/error";
    }

    if (newPass != null) {
      user.setPassword(encoder.encode(newPass));
    } else {
      return "redirect:/error";
    }

    Authentication token = new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword(),
        authentication.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(token);

    userRepository.save(user);
    return "redirect:/home";
  }
}
// @PostMapping("/settings/profile")@PostMapping("/settings/security")@PostMapping("/settings/language")
