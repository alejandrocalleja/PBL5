package eus.blankcard.decklearn;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import eus.blankcard.decklearn.controller.ProfileController;

@WebMvcTest
public class ProfileControllerTest {
  @Autowired
  private ProfileController controller;

  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
}
