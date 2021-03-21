package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Deck;
import ch.cas.html5.multicardgame.entity.Deckelement;
import ch.cas.html5.multicardgame.repository.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeckServiceImpl {

    private final DeckRepository deckRepository;

    public DeckServiceImpl(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> retrieveDecks() {
        return deckRepository.findAll();
    }

    public Deck getDeck(String deckId) {
        Optional<Deck> optDeck = deckRepository.findById(deckId);
        return optDeck.orElse(null);
    }

    public void saveDeck(Deck deck){
        deckRepository.save(deck);
    }

    public void deleteDeck(String deckId){
        deckRepository.deleteById(deckId);
    }

    public void addDeckelement(String deckId, Deckelement deckelement){
        Optional<Deck> optDeck = deckRepository.findById(deckId);
        optDeck.ifPresent(deck -> deck.getDeckelements().add(deckelement));

    }


}
