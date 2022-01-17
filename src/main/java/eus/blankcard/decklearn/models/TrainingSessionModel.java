package eus.blankcard.decklearn.models;

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
@Table( name = "training_session")
public class TrainingSessionModel {
    
    @Id
    @Column( name = "training_session_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="training_id")
    private TrainingModel training;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    List<ResultsModel> results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrainingModel getTraining() {
        return training;
    }

    public void setTraining(TrainingModel training) {
        this.training = training;
    }

    public List<ResultsModel> getResults() {
        return results;
    }

    public void setResults(List<ResultsModel> results) {
        this.results = results;
    }

    
}
