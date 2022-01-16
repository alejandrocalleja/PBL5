package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import eus.blankcard.decklearn.models.user.UserModel;


@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @Test
  public void shouldReturnLoginPage() throws Exception {
    String url = "/register";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void shouldReturnSuccessRegister() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken  csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url = "/register";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    
    String name = "MVC";
    String surname = "Test";
    String email = "mvctest@decklearn.eus";
    String purpose = "Testing";
    String country = "World";
    String username = "testRegister";
    String password = "testPass";

    params.add("name", name);
    params.add("surname", surname);
    params.add("email", email);
    params.add("purpose", purpose);
    params.add("country", country);
    params.add("username", username);
    params.add("password", password);

    UserModel userModel = new UserModel();
    userModel.setName(name);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
    .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/home"));
  }

  @Test
  public void shouldReturnFailedLogin() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken  csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url = "/login";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    String username = "testUser";
    String password = "testIncorrect";

    params.add("username", username);
    params.add("password", password);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
    .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  public void shouldReturnNullUser() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken  csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url = "/login";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    String username = "testUserIncorrect";
    String password = "testIncorrect";

    params.add("username", username);
    params.add("password", password);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
    .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }
}