package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;

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
      "/testUser/following"
  })
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnWebPage(String url) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(MockMvcResultMatchers.status().isOk());
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
