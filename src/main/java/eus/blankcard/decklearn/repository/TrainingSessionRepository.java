package eus.blankcard.decklearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eus.blankcard.decklearn.models.TrainingSessionModel;

public interface TrainingSessionRepository extends JpaRepository<TrainingSessionModel, Integer> {

    @Query(value="SELECT * FROM TrainingSession t WHERE t.training_id = ?1 ORDER BY t.training_session_date DESC LIMIT ?2",  nativeQuery = true)
    TrainingSessionModel findByTrainingIdAndDateWithLimit(Integer trainingId, Integer index);

}
