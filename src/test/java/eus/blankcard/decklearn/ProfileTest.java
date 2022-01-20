package eus.blankcard.decklearn;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
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
public class ProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnUserPage() throws Exception {
        String url = "/testUser";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnFollowersPage() throws Exception {
        String url = "/testUser/followers";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnFollowingPage() throws Exception {
        String url = "/testUser/following";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnSavedPage() throws Exception {
        String url = "/testUser/saved";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnSessionsPage() throws Exception {
        String url = "/testUser/sessions";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnSuccessFollowUnfollow() throws Exception {
        String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken  csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

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
    public void shouldReturnUserPageRedirectionError() throws Exception {
        String url = "/testIncorrect";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void shouldReturnFollowersPageRedirectionError() throws Exception {
        String url = "/testUser/followers";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void shouldReturnFollowingPageRedirectionError() throws Exception {
        String url = "/testUser/following";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnSavedPageRedirectionError() throws Exception {
        String url = "/testIncorrect/saved";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void shouldReturnSessionsPageRedirectionError() throws Exception {
        String url = "/testIncorrect/sessions";

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
