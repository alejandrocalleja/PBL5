package eus.blankcard.decklearn.models.card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eus.blankcard.decklearn.models.ResultsModel;

@Entity
@Table( name = "card_responses")
public class CardResponseModel {
    
    @Id
    @Column( name = "card_responses_id ")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="results_id")
    private ResultsModel results;

    @ManyToOne
    @JoinColumn(name="card_id")
    private CardModel card;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ResultsModel getResults() {
        return results;
    }

    public void setResults(ResultsModel results) {
        this.results = results;
    }

    public CardModel getCard() {
        return card;
    }

    public void setCard(CardModel card) {
        this.card = card;
    }


    
}