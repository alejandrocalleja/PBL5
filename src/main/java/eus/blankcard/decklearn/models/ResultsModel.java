package eus.blankcard.decklearn.models;

import java.sql.Time;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import eus.blankcard.decklearn.models.card.CardModel;
import eus.blankcard.decklearn.models.card.CardResponseModel;

@Entity
@Table( name = "results" )
public class ResultsModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column( name = "results_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "training_session_id")
    private TrainingSessionModel trainingSession;


    @ManyToOne
    @JoinColumn(name = "card_id")
    private CardModel card;

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    List<CardResponseModel> cardResponses;    @Column( name = "box_number")
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
    }    public void setBoxNumber(int boxNumber) {
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

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }
}
