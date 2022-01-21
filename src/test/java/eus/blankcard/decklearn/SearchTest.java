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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMockMvc() throws Exception {
        assertNotNull(mockMvc);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnSearchResults() throws Exception {
        String url = "/deck/search";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String searchName = "girl";

        params.add("searchName", searchName);

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .params(params))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shoulNotReturnSearchResults() throws Exception {
        String url = "/deck/search";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        String searchName = "";

        params.add("searchName", searchName);

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .params(params))
                .andExpect(redirectedUrl("/home"));
    }
}
