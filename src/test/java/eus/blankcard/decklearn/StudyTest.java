package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    //   String url = "/deck/1/study";
  
    //   mockMvc.perform(MockMvcRequestBuilders.post(url))
    //       .andExpect(redirectedUrl("/study/1"));
    // }
}
