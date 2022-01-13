package eus.blankcard.decklearn.repository.user;

import eus.blankcard.decklearn.models.UserModel;

public interface UserRepositoryCustom {
    UserModel loadUser(String username, String password);
    void addUserEncrypted(UserModel user);
}