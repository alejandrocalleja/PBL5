package eus.blankcard.decklearn.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "card_responses")
public class CardResponseModel {
    
    @Id
    @Column( name = "card_responses_id ")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="results_id")
    private ResultsModel result;

    @ManyToOne
    @JoinColumn(name="card_id")
    private CardModel card;

    boolean correct;

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
}