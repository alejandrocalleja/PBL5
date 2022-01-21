package eus.blankcard.decklearn.models.deck;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "deck_type")
public class DeckTypeModel {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="deck_type_id")
    private Integer id;

    private String description;

    @ManyToMany(mappedBy = "types")
    private List<DeckModel> decks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DeckModel> getDecks() {
        return decks;
    }

    public void setDecks(List<DeckModel> decks) {
        this.decks = decks;
    }

    
}
