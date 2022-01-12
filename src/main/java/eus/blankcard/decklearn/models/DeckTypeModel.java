package eus.blankcard.decklearn.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "deck_type")
public class DeckTypeModel {
    
    @Id
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
}
