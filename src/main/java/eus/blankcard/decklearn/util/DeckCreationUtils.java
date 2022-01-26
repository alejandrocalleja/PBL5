package eus.blankcard.decklearn.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.card.CardModel;
import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.deck.DeckTypeModel;
import eus.blankcard.decklearn.repository.CardRepository;
import eus.blankcard.decklearn.repository.DeckTypeRepository;
import eus.blankcard.decklearn.repository.deck.DeckRepository;

@Component
public class DeckCreationUtils {

  @Autowired
  DeckRepository deckRepository;

  @Autowired
  CardRepository cardRepository;

  @Autowired
  DeckTypeRepository deckTypeRepository;

  public void saveCard(String question, String answer, DeckModel deck) {
    CardModel card = new CardModel();
    card.setQuestion(question);
    card.setAnswer(answer);
    card.setDeck(deck);
    card = cardRepository.save(card);

    deck.addCard(card);
    deckRepository.save(deck);
  }

  public void saveDeckType(String description, DeckModel deck) {
    DeckTypeModel type;

    type = deckTypeRepository.findByDescription(description);

    if (type == null) {
      type = new DeckTypeModel();
      type.setDescription(description);
      type.addDeck(deck);
    } else {
      type.addDeck(deck);
    }

    deck.addType(type);
    type = deckTypeRepository.save(type);
    deckRepository.save(deck);
  }

  public String checkAction(HttpServletRequest req, HttpServletResponse res, DeckModel deck, String action) {
    String redirectUrl = "redirect:/create/deck/" + deck.getId();

    switch (action) {
      case "Save Card":
        String question = req.getParameter("question");
        String answer = req.getParameter("answer");

        saveCard(question, answer, deck);
        break;
      case "Add Type":
        String description = req.getParameter("type");

        saveDeckType(description, deck);
        break;
      case "Save Deck":
        deck = deckRepository.save(deck);

        redirectUrl = "redirect:/deck/" + deck.getId();
        break;

      default:
        redirectUrl = "redirect:/error";
        break;
    }

    return redirectUrl;
  }

}
