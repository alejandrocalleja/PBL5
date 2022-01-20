package eus.blankcard.decklearn.repository.user;

import java.util.List;

import eus.blankcard.decklearn.models.UserModel;

public interface UserRepositoryCustom {
    // UserModel loadUser(String username, String password);
    // void addUserEncrypted(UserModel user);
    // int countFollowers(Integer user_id);
    // int countFollowing(Integer user_id);
    // List<UserModel> loadFollowers(Integer user_id);
    // List<UserModel> loadFollowing(Integer user_id);

    boolean checkFollowed(UserModel user, UserModel targetUser);
    // void follow(UserModel user, UserModel targetUser);
    // void unfollow(UserModel user, UserModel targetUser);

}