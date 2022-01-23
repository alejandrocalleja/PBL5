package eus.blankcard.decklearn.util;

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

        if(type == null) {
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
    
}
