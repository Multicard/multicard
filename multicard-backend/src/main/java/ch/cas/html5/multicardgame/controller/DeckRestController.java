package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Deck;
import ch.cas.html5.multicardgame.entity.Deckelement;
import ch.cas.html5.multicardgame.implementation.DeckServiceImpl;
import ch.cas.html5.multicardgame.implementation.DeckelementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class DeckRestController {
    @Autowired
    private DeckServiceImpl deckService;
    public void setDeckService(DeckServiceImpl deckService) {
        this.deckService = deckService;
    }

    @Autowired
    private DeckelementServiceImpl deckElementService;
    public void setDeckelementService(DeckelementServiceImpl deckElementService) {
        this.deckElementService = deckElementService;
    }

    @GetMapping("/api/Decks")
    public List<Deck> getDecks() {
        List<Deck> decks = deckService.retrieveDecks();
        return decks;
    }

    @GetMapping("/api/Decks/{DeckId}")
    public Deck getDeck(@PathVariable(name="DeckId")String deckId) {
        return deckService.getDeck(deckId);
    }

    @PostMapping("/api/Decks")
    public void saveDeck(Deck card){
        deckService.saveDeck(card);
    }

    @DeleteMapping("/api/Decks/{DeckId}")
    public void deleteDeck(@PathVariable(name="DeckId")String deckId){
        deckService.deleteDeck(deckId);
    }

    @PutMapping("/api/Decks/{DeckId}")
    public void addDeckelement(@RequestBody Deckelement deckelement,
                           @PathVariable(name="DeckId")String deckId){
        Deck emp = deckService.getDeck(deckId);
        if(emp != null){
            if (deckelement.getDeck() == null){
                deckelement.setDeck(emp);
            }
            Deckelement element = deckElementService.saveDeckelement(deckelement);
            deckService.addDeckelement(deckId,element);
        }

    }



}
