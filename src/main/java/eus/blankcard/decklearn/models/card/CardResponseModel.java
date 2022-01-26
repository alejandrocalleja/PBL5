package eus.blankcard.decklearn.models.card;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eus.blankcard.decklearn.models.ResultsModel;

@Entity
@Table( name = "card_responses")
public class CardResponseModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column( name = "card_responses_id ")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="results_id")
    private ResultsModel result;

    @ManyToOne
    @JoinColumn(name="card_id")
    private CardModel card;
    
    boolean correct;
    
    @Column( name = "response_date")
    Date responseDate;

    @Column( name = "response_time")
    Time responseTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ResultsModel getResult() {
        return result;
    }

    public void setResult(ResultsModel result) {
        this.result = result;
    }

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public Time getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Time responseTime) {
        this.responseTime = responseTime;
    }
}