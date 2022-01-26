package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "/settings",
      "/settings/profile",
      "/settings/security",
      "/settings/language"
  })
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnWebPage(String url) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "/settings",
      "/settings/profile",
      "/settings/security",
      "/settings/language"
  })
  @WithMockUser(username = "fakeUser", roles = "USER")
  void shouldNotReturnWebPage(String url) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldLogout() throws Exception {
    String url = "/logout";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(redirectedUrl("/login?handler=logout"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldModifyProfile() throws Exception {
    String url = "/settings/profile";
    String name, surname, username, postalCode, country;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    name = "Test";
    surname = "Profile";
    username = "testUser";
    postalCode = "01213";
    country = "Basque Country";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("name", name);
    params.add("surname", surname);
    params.add("username", username);
    params.add("postalCode", postalCode);
    params.add("country", country);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/home"));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "null, testPass, /error",
      "test@decklearn.eus, null, /error",
      "test@decklearn.eus, testPass, /home"
  }, nullValues = { "null" })
  @WithMockUser(username = "testUser", roles = "USER")
  void SecurityTabMultipleOptions(String email, String newPass, String rUrl) throws Exception {
    String url = "/settings/security";

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("email", email);
    params.add("password", newPass);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl(rUrl));
  }

}
