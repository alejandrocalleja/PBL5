package eus.blankcard.decklearn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.repository.user.UserRepository;

@Controller
public class HomeController {

  @Autowired
  UserRepository userRepository;

  @GetMapping("/home")
  public String getHome(HttpServletRequest req, HttpServletResponse response) {

    return "home";
  }
}
