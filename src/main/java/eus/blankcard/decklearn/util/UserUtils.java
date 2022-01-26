package eus.blankcard.decklearn.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;

@Component
public class UserUtils {

  public boolean checkFollowed(UserModel loggedUser, UserModel targetUser) {
    return loggedUser.getFollowed().contains(targetUser);
  }

  public Set<UserModel> getFollowingFromList(UserModel loggedUser, Set<UserModel> targetList) {
    return targetList.stream().filter(user -> loggedUser.getFollowed().contains(user)).collect(Collectors.toSet());
  }

  public Set<DeckModel> getFollowingDecks(UserModel loggedUser) {
    return loggedUser.getFollowed().stream().map(user -> user.getDecks()).flatMap(Set::stream).collect(Collectors.toSet());
  }
}
