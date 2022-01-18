package eus.blankcard.decklearn.models;

import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( name = "results" )
public class ResultsModel {
    
    @Id
    @Column( name = "results_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="training_session_id")
    private TrainingSessionModel trainingSession;

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    private List<CardResponseModel> cardResponses;

    @Column( name = "box_number")
    private int boxNumber;

    @Column( name = "error_count")
    private int errorCount;

    @Column( name = "avg_response_time")
    private Time avgTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrainingSessionModel getTrainingSession() {
        return trainingSession;
    }

    public void setTrainingSession(TrainingSessionModel trainingSession) {
        this.trainingSession = trainingSession;
    }

    public List<CardResponseModel> getCardResponses() {
        return cardResponses;
    }

    public void setCardResponses(List<CardResponseModel> cardResponses) {
        this.cardResponses = cardResponses;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public Time getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(Time avgTime) {
        this.avgTime = avgTime;
    }
}
