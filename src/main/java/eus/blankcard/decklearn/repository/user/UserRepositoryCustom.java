package eus.blankcard.decklearn.repository.user;

import eus.blankcard.decklearn.models.UserModel;

public interface UserRepositoryCustom {

  boolean checkFollowed(UserModel user, UserModel targetUser);

}