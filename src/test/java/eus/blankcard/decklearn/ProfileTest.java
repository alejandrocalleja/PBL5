package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "/testUser",
      "/testUser/followers",
      "/testUser/following",
      "/testUser/saved",
      "/testUser/sessions",
      "/testIncorrect/saved",
      "/testIncorrect/sessions"
  })
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnWebPage(String url) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnSuccessFollowUnfollow() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url = "/Emeraldoff/follow";

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/Emeraldoff"));

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/Emeraldoff"));
  }

  @Test
  @WithAnonymousUser
  void shouldReturnUserPageRedirectionError() throws Exception {
    String url = "/testIncorrect";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
  }

  @Test
  @WithAnonymousUser
  void shouldReturnFollowersPageRedirectionError() throws Exception {
    String url = "/testUser/followers";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
  }

  @Test
  @WithAnonymousUser
  void shouldReturnFollowingPageRedirectionError() throws Exception {
    String url = "/testUser/following";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isFound());
  }
}
