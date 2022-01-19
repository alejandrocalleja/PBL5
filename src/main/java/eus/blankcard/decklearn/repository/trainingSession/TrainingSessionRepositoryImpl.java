package eus.blankcard.decklearn.repository.trainingSession;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eus.blankcard.decklearn.models.TrainingSessionModel;

@Repository
@Transactional(readOnly = true)
public class TrainingSessionRepositoryImpl implements TrainingSessionRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public TrainingSessionModel findByTrainingIdAndDateWithLimit(Integer trainingId, Integer index) {
        TrainingSessionModel trainingSession = null;
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM training_session t WHERE t.training_id = ?1 ORDER BY t.training_session_date DESC limit ?2",
                TrainingSessionModel.class);

                query.setParameter(1, trainingId);
                query.setParameter(2, index);

        try {
            trainingSession = (TrainingSessionModel) query.getSingleResult();
        } catch (Exception e) {
        }

        return trainingSession;
    }
    
    @Override
    public TrainingSessionModel findLastSession(int trainingId) {
        TrainingSessionModel trainingSession = null;
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM training_session t WHERE t.training_id=?1 ORDER BY t.training_session_id DESC limit 1",
                TrainingSessionModel.class);
            query.setParameter(1, trainingId);
        try {
            trainingSession = (TrainingSessionModel) query.getSingleResult();
        } catch (Exception e) {
        }

        return trainingSession;
    }
}
