package eus.blankcard.decklearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.CardResponseModel;

public interface CardResponseRepository extends JpaRepository<CardResponseModel, Integer> {
    
}
