package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Deck;
import ch.cas.html5.multicardgame.entity.Deckelement;
import ch.cas.html5.multicardgame.services.DeckServiceImpl;
import ch.cas.html5.multicardgame.services.DeckelementServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class DeckRestController {
    private final DeckServiceImpl deckService;
    private final DeckelementServiceImpl deckElementService;

    public DeckRestController(DeckServiceImpl deckService, DeckelementServiceImpl deckElementService) {
        this.deckService = deckService;
        this.deckElementService = deckElementService;
    }

    @GetMapping("/api/Decks")
    public List<Deck> getDecks() {
        return deckService.retrieveDecks();
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
