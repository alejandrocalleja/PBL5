package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
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
public class HomeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnHomePage() throws Exception {
        String url = "/home";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void shouldReturnRedirectionError() throws Exception {
        String url = "/home";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}