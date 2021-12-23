package eus.blankcard.decklearn.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer>, UserRepositoryCustom {
    
    UserModel findByUsername(String username);

}
