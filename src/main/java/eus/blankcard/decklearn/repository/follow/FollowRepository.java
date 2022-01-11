package eus.blankcard.decklearn.repository.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.FollowRelation;

public interface FollowRepository extends JpaRepository<FollowRelation, Integer>, FollowRepositoryCustom{
    
}
