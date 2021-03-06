package eus.blankcard.decklearn.repository.trainingsession;

import org.springframework.data.jpa.repository.JpaRepository;

import eus.blankcard.decklearn.models.TrainingSessionModel;

public interface TrainingSessionRepository extends JpaRepository<TrainingSessionModel, Integer> {
}
