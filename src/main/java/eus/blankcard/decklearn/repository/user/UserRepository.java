package eus.blankcard.decklearn.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.user.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    
    UserModel findByUsername(String username);
}
