package eus.blankcard.decklearn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;


@SpringBootTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void loadUserLogin() {
        UserModel expectedUser = new UserModel();
        expectedUser.setId(4);
        expectedUser.setEmail("a");
        expectedUser.setName("a");
        expectedUser.setPassword("a");
        expectedUser.setUsername("a");

        UserModel actualUser = userRepository.loadUser("a", "a");

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void createUserTest () {
        UserModel user = new UserModel();
        user.setName("prueba");
        user.setUsername("prueba");
        user.setEmail("prueba@gmail.com");
        user.setPassword(encoder.encode("prueba"));

        UserModel retUser = userRepository.save(user);

        assertTrue(retUser.getPassword().equalsIgnoreCase(user.getPassword()));
    }
}