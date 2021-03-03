package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Deck;
import ch.cas.html5.multicardgame.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeckServiceImpl {

    @Autowired
    private DeckRepository deckRepository;

    public void setDeckRepository(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> retrieveDecks() {
        List<Deck> decks = deckRepository.findAll();
        return decks;
    }

    public Deck getDeck(String deckId) {
        Optional<Deck> optDeck = deckRepository.findById(deckId);
        if (optDeck.isPresent()) {
            return optDeck.get();
        }
        return null;
    }

    public Deck saveDeck(Deck deck){
        return deckRepository.save(deck);
    }

    public void deleteDeck(String deckId){
        deckRepository.deleteById(deckId);
    }


}
