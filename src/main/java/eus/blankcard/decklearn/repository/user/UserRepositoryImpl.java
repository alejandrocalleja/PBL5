package eus.blankcard.decklearn.repository.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eus.blankcard.decklearn.models.user.UserModel;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public boolean checkFollowed(UserModel user, UserModel targetUser) {
    // System.out.println("Checking followed");
    return user.getFollowed().contains(targetUser);
  }
}