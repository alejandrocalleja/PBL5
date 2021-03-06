package eus.blankcard.decklearn.models.card;

import java.util.ArrayList;
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

import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.deck.DeckModel;

@Entity
@Table(name = "card")
public class CardModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "card_id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "deck_id")
  private DeckModel deck;

  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
  List<ResultsModel> results = new ArrayList<>();

  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
  List<CardResponseModel> cardResponses;

  private String question;

  private String answer;

  @Column(name = "img_path")
  private String imgPath;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public DeckModel getDeck() {
    return deck;
  }

  public void setDeck(DeckModel deck) {
    this.deck = deck;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getImgPath() {
    return imgPath;
  }

  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  public List<CardResponseModel> getCardResponses() {
    return cardResponses;
  }

  public void setCardResponses(List<CardResponseModel> cardResponses) {
    this.cardResponses = cardResponses;
  }

  public List<ResultsModel> getResults() {
    return results;
  }

  public void setResults(List<ResultsModel> results) {
    this.results = results;
  }
}
