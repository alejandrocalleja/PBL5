package eus.blankcard.decklearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.RegisterUtils;

@Controller
public class RegisterController {

  @Autowired
  RegisterUtils registerUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @GetMapping("/register")
  public String registerForm() {
    return "register";
  }

  @PostMapping("/register")
  public String registerSubmit(UserModel user) {
    user.setPassword(encoder.encode(user.getPassword()));
    user.setImgPath("/images/user/default.png");
    user = userRepository.save(user);
    registerUtils.setDefaultFollower(user);

    return "redirect:/login";
  }
}
