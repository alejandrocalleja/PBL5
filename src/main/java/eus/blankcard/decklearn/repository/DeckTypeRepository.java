package eus.blankcard.decklearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.deck.DeckTypeModel;

public interface DeckTypeRepository extends JpaRepository<DeckTypeModel, Integer> {
    DeckTypeModel findByDescription(String description);

}
