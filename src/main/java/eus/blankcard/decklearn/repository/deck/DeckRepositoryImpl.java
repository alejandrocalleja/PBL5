package eus.blankcard.decklearn.repository.deck;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import eus.blankcard.decklearn.models.deck.DeckModel;

@Repository
public class DeckRepositoryImpl implements DeckRepositoryCustom {

  // @PersistenceContext
  // EntityManager entityManager;

  // @Override
  // public List<DeckModel> findByCreator(Integer creatorId) {
  //   List<DeckModel> deckList = new ArrayList<>();

  //   TypedQuery<DeckModel> query = entityManager.createQuery("SELECT * from deck WHERE creator_id=?1",
  //       DeckModel.class);

  //   query.setParameter(1, creatorId);

  //   try {
  //     deckList = query.getResultList();
  //   } catch (Exception e) {
  //     System.out.println("Failed to load decks from user " + creatorId);
  //   }

  //   return deckList;
  // }

}
