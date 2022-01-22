package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyTest {
    
    @Autowired
    private MockMvc mockMvc;
  
    @Test
    void shouldCreateMockMvc() {
      assertNotNull(mockMvc);
    }

    // @Test
    // @WithMockUser(username = "testUser", roles = "USER")
    // void shouldReturnDeckViewPage() throws Exception {
    //   String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    //   HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    //   CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    //   String url = "/deck/1/study";
  
    //   mockMvc.perform(MockMvcRequestBuilders.post(url)
    //   .sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))  
    //       .andExpect(redirectedUrl("/study/1"));
    // }
}
