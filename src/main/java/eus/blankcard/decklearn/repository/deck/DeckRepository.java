package eus.blankcard.decklearn.repository.deck;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.deck.DeckModel;

public interface DeckRepository extends JpaRepository<DeckModel, Integer>, DeckRepositoryCustom {

    List<DeckModel> findByTitleContaining(String title, Pageable limit);
}
