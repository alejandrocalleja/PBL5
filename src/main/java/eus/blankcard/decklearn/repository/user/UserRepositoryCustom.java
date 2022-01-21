package eus.blankcard.decklearn.repository.user;

import java.util.List;
import eus.blankcard.decklearn.models.user.UserModel;

public interface UserRepositoryCustom {
  boolean checkFollowed(UserModel user, UserModel targetUser);

}