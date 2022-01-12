package eus.blankcard.decklearn.repository.deck;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.DeckModel;

public interface DeckRepository extends JpaRepository<DeckModel, Integer>, DeckRepositoryCustom {
}
