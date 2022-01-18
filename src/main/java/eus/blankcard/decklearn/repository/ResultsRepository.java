package eus.blankcard.decklearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.ResultsModel;

public interface ResultsRepository extends JpaRepository<ResultsModel, Integer> {
    
}
