package eus.blankcard.decklearn.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    String username = authentication.getName();
    
    UserModel user = userRepository.findByUsername(username);

    HttpSession session = request.getSession();
    session.setAttribute("userImg", user.getImgPath());

    response.sendRedirect("/home");
  }

}