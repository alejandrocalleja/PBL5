package eus.blankcard.decklearn.gamification;

import eus.blankcard.decklearn.models.TrainingSessionModel;

public class SessionCardManager {
    private TrainingSessionModel trainingSession;
    private Buffer buffer;

    public SessionCardManager(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;

        buffer = new Buffer();
    }

    public TrainingSessionModel getTrainingSessionId() {
        return trainingSession;
    }

    public void setTrainingSessionId(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    // public saveCardResponse(CardResponseModel cardResponse) {

    // }
        
    // public CardModel getNextCard() {

    // }
}
