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
    private ResultsModel results;

    @ManyToOne
    @JoinColumn(name="card_id")
    private CardModel card;
}