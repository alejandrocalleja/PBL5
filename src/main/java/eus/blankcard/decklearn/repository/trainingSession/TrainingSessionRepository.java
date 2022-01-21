package eus.blankcard.decklearn.repository.trainingSession;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.TrainingSessionModel;

public interface TrainingSessionRepository extends JpaRepository<TrainingSessionModel, Integer>, TrainingSessionRepositoryCustom {

    // @Query(value="SELECT * FROM TrainingSessionModel t WHERE t.training_id = ?1 ORDER BY t.training_session_date DESC",  nativeQuery = true)
    // TrainingSessionModel findByTrainingIdAndDateWithLimit(Integer trainingId, Integer index);

    // @Query(value="SELECT * FROM training_session t ORDER BY t.training_session_id DESC limit 1",  nativeQuery = true)
    // TrainingSessionModel findLastSession();
}
