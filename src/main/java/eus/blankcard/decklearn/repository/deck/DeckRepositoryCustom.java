package eus.blankcard.decklearn.repository.deck;

import java.util.List;

import eus.blankcard.decklearn.models.deck.DeckModel;

public interface DeckRepositoryCustom {
    List<DeckModel> findByCreator(Integer creatorId);
    
}
