package eus.blankcard.decklearn.util;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.user.UserModel;

@Component
public class UserUtils {

    public boolean checkFollowed(UserModel loggedUser, UserModel targetUser) {
        return loggedUser.getFollowed().contains(targetUser);
    }
    
}
