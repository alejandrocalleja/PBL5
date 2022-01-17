package eus.blankcard.decklearn.gamification;

public class SessionManager {
    private int trainingSessionId;
    private Buffer buffer;

    public SessionManager(int trainingSessionId) {
        this.trainingSessionId = trainingSessionId;

        buffer = new Buffer();
    }

    public int getTrainingSessionId() {
        return trainingSessionId;
    }

    public void setTrainingSessionId(int trainingSessionId) {
        this.trainingSessionId = trainingSessionId;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }
}
