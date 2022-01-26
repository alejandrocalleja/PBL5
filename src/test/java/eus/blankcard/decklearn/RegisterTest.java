package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @Test
  void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @Test
  void shouldReturnRegisterPage() throws Exception {
    String url = "/register";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void shouldRetunDate() throws Exception {

    UserModel u = new UserModel();
    u.setBirthDate(new Date(979772400));

    assertEquals(u.getBirthDate(), new Date(979772400));
  }

  @Test
  void shouldReturnSuccessRegister() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url = "/register";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    String name = "MVC";
    String surname = "Test";
    String email = "mvctest@decklearn.eus";
    String username = encoder.encode("testRegister");
    username = username.substring(0, Math.min(username.length(), 10));
    String purpose = "Testing";
    String country = "World";
    String postalCode = "01234";
    String password = encoder.encode("testPass");

    params.add("name", name);
    params.add("surname", surname);
    params.add("email", email);
    params.add("username", username);
    params.add("purpose", purpose);
    params.add("country", country);
    params.add("postalCode", postalCode);
    params.add("password", password);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(),
            csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/login"));
  }

}