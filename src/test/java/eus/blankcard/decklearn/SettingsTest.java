package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
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
public class SettingsTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldReturnSettingsPage() throws Exception {
    String url = "/settings";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldReturnProfilePage() throws Exception {
    String url = "/settings/profile";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldReturnSecurityPage() throws Exception {
    String url = "/settings/security";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldReturnLanguagePage() throws Exception {
    String url = "/settings/language";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "fakeUser", roles = "USER")
  public void shouldNotReturnProfilePage() throws Exception {
    String url = "/settings/profile";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "fakeUser", roles = "USER")
  public void shouldNotReturnSecurityPage() throws Exception {
    String url = "/settings/security";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "fakeUser", roles = "USER")
  public void shouldNotReturnLanguagePage() throws Exception {
    String url = "/settings/language";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldModifyProfile() throws Exception {
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

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldNotModifyEmail() throws Exception {
    String url = "/settings/security";
    String email, newPass;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    email = null;
    newPass = "testPass";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("email", email);
    params.add("password", newPass);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldNotModifyPass() throws Exception {
    String url = "/settings/security";
    String email, newPass;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    email = "test@decklearn.eus";
    newPass = null;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("email", email);
    params.add("password", newPass);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldModifySecurity() throws Exception {
    String url = "/settings/security";
    String email, newPass;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    email = "test@decklearn.eus";
    newPass = "testPass";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    params.add("email", email);
    params.add("password", newPass);

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/home"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  public void shouldLogout() throws Exception {
    String url = "/logout";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(redirectedUrl("/login?logout"));
  }

}
