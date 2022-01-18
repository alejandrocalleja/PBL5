package eus.blankcard.decklearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eus.blankcard.decklearn.models.TrainingModel;

public interface TrainingRepository extends JpaRepository<TrainingModel, Integer> {
    
    @Query(value="SELECT * FROM Training t WHERE t.user_id = ?1 AND t.deck_id = ?2",  nativeQuery = true)
    TrainingModel findByUserIdInAndDeckId(Integer userId, Integer deckId);
}
