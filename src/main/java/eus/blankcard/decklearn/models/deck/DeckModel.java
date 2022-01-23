package eus.blankcard.decklearn.models.deck;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.card.CardModel;
import eus.blankcard.decklearn.models.user.UserModel;

@Entity
@Table(name="deck")
public class DeckModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="deck_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="creator_id")
    private UserModel creator;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    List<CardModel> cards = new ArrayList<>();

    @ManyToMany
    @JoinTable( name = "type_relation",
                joinColumns = { @JoinColumn( name = "deck_id") },
                inverseJoinColumns = {@JoinColumn( name = "deck_type_id")}
                )
    private List<DeckTypeModel> types = new ArrayList<>();

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    List<TrainingModel> trainings = new ArrayList<>();

    @ManyToMany(mappedBy = "savedDecks")
    private List<UserModel> savers = new ArrayList<>();

    private String title;

    private String description;

    @Column( name = "img_path")
    private String imgPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public List<DeckTypeModel> getTypes() {
        return types;
    }

    public void setTypes(List<DeckTypeModel> types) {
        this.types = types;
    }

    public void addType(DeckTypeModel type) {
        this.types.add(type);
    }

    public List<UserModel> getSavers() {
        return savers;
    }

    public void setSavers(List<UserModel> savers) {
        this.savers = savers;
    }

    public List<CardModel> getCards() {
        return cards;
    }

    public void setCards(List<CardModel> cards) {
        this.cards = cards;
    }

    public void addCard(CardModel card) {
        this.cards.add(card);
    }

    public List<TrainingModel> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingModel> trainings) {
        this.trainings = trainings;
    }
}
