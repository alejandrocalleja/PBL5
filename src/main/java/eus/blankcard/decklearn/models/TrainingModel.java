package eus.blankcard.decklearn.models;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;

@Entity
@Table( name = "training")
public class TrainingModel {
    
    @Id
    @Column( name = "training_id" )
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name="deck_id")
    private DeckModel deck;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    List<TrainingSessionModel> trainingSessions;

    private Date training_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public DeckModel getDeck() {
        return deck;
    }

    public void setDeck(DeckModel deck) {
        this.deck = deck;
    }

    public Date getTraining_date() {
        return training_date;
    }

    public void setTraining_date(Date training_date) {
        this.training_date = training_date;
    }

    public List<TrainingSessionModel> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSessionModel> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }
}
