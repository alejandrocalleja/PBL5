package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
class DeckTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldCreateMockMvc() {
    assertNotNull(mockMvc);
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnDeckViewPage() throws Exception {
    String url = "/deck/1";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "Alroden", roles = "USER")
  void shouldReturnDeckStatsViewPage() throws Exception {
    String url = "/deck/153/stats";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnDeckCreationForm() throws Exception {
    String url = "/create/deck";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnDeckEditForm() throws Exception {
    String url = "/create/deck/";

    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "/deck/1/save",
      "/deck/1/save"
  })
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldSaveDeckAndRemove(String url) throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/deck/1"));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "/deck/999/save"
  })
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldNotSaveDeck(String url) throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldCreateAndRemoveDeck() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url;

    String deckId = createDeckSaveCard(TOKEN_ATTR_NAME, csrfToken);

    // /CREATE/DECK/DECKID --- GET
    url = "/create/deck/" + deckId;
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());

    editDeckSaveCard(TOKEN_ATTR_NAME, csrfToken, deckId);

    editDeckAddType(TOKEN_ATTR_NAME, csrfToken, deckId);

    saveDeck(TOKEN_ATTR_NAME, csrfToken, deckId);

    errorDefaultActionDeck(TOKEN_ATTR_NAME, csrfToken, deckId);

    // /DECK/DECKID/REMOVE --- GET
    url = "/deck/" + deckId + "/remove";
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/testUser"));
  }

  public String createDeckSaveCard(String TOKEN_ATTR_NAME, CsrfToken csrfToken) throws Exception {
    String url, title, description, action, question, answer;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    url = "/create/deck";
    title = "testDeck";
    description = "Testing deck";
    action = "Save Card";
    question = "Is this a testing card?";
    answer = "Yes, it is";

    params.add("title", title);
    params.add("description", description);
    params.add("action", action);
    params.add("question", question);
    params.add("answer", answer);

    // /CREATE/DECK/ --- POST (Save Card)
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andReturn();

    String[] redirectedUrl = result.getResponse().getRedirectedUrl().split("/");

    return redirectedUrl[3];
  }

  public void editDeckSaveCard(String TOKEN_ATTR_NAME, CsrfToken csrfToken, String deckId) throws Exception {
    String url, title, description, action, question, answer;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    url = "/create/deck/" + deckId;
    title = "testDeckEdit";
    description = "Testing deck modified";
    action = "Save Card";
    question = "Is this a testing card modified?";
    answer = "Yes, it is";

    params.add("title", title);
    params.add("description", description);
    params.add("action", action);
    params.add("question", question);
    params.add("answer", answer);

    // /CREATE/DECK/ --- POST (Save Card)
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl(url));
  }

  public void editDeckAddType(String TOKEN_ATTR_NAME, CsrfToken csrfToken, String deckId) throws Exception {
    String url, title, description, action, question, answer, type;
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    url = "/create/deck/" + deckId;
    title = "testDeckEdit";
    description = "Testing deck modified";
    action = "Add Type";
    question = "Is this a testing card modified?";
    answer = "Yes, it is";
    type = "Testing type";

    params.add("title", title);
    params.add("description", description);
    params.add("action", action);
    params.add("question", question);
    params.add("answer", answer);
    params.add("type", type);

    // /CREATE/DECK/ --- POST (Save Card)
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl(url));
  }

  public void saveDeck(String TOKEN_ATTR_NAME, CsrfToken csrfToken, String deckId) throws Exception {
    String url, title, description, action, question, answer;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    url = "/create/deck/" + deckId;
    title = "testDeckEdit";
    description = "Testing deck modified";
    action = "Save Deck";
    question = "Is this a testing card modified?";
    answer = "Yes, it is";

    params.add("title", title);
    params.add("description", description);
    params.add("action", action);
    params.add("question", question);
    params.add("answer", answer);

    // /CREATE/DECK/ --- POST (Save Card)
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/deck/" + deckId));
  }

  public void errorDefaultActionDeck(String TOKEN_ATTR_NAME, CsrfToken csrfToken, String deckId) throws Exception {
    String url, title, description, action, question, answer;

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    url = "/create/deck/" + deckId;
    title = "testDeckEdit";
    description = "Testing deck modified";
    action = "errorAction";
    question = "Is this a testing card modified?";
    answer = "Yes, it is";

    params.add("title", title);
    params.add("description", description);
    params.add("action", action);
    params.add("question", question);
    params.add("answer", answer);

    // /CREATE/DECK/ --- POST (Save Card)
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken())
        .params(params))
        .andExpect(redirectedUrl("/error"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldNotCreateAndRemoveDeck() throws Exception {
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    String url;

    String deckId = "1";

    // /CREATE/DECK/DECKID --- GET
    url = "/create/deck/" + deckId;
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(redirectedUrl("/error"));

    // /DECK/DECKID/REMOVE --- GET
    url = "/deck/" + deckId + "/remove";
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/error"));

    deckId = "999";

    url = "/deck/" + deckId + "/remove";
    mockMvc.perform(MockMvcRequestBuilders.post(url)
        .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
        .andExpect(redirectedUrl("/error"));
  }
}
