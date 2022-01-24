package eus.blankcard.decklearn.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;

@Component
public class RegisterUtils {
    
    @Autowired
    private UserRepository userRepository;

    public void setDefaultFollower(UserModel user) {

        UserModel officialAccount = userRepository.findByUsername("DeckLearnOfficial");

        if(officialAccount != null) {
            user.addFollowed(officialAccount);
            officialAccount.addFollower(user);

            userRepository.save(user);
            userRepository.save(officialAccount);
        }
    }
}
