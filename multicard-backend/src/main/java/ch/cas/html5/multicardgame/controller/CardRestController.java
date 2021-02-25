package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CardRestController {
    @Autowired
    private CardService CardService;

    public void setCardService(CardService CardService) {
        this.CardService = CardService;
    }

    @GetMapping("/api/Cards")
    public List<Card> getCards() {
        List<Card> Cards = CardService.retrieveCards();
        return Cards;
    }

    @GetMapping("/api/Cards/{CardId}")
    public Card getCard(@PathVariable(name="CardId")Long CardId) {
        return CardService.getCard(CardId);
    }

    @PostMapping("/api/Cards")
    public void saveCard(Card Card){
        CardService.saveCard(Card);
        System.out.println("Card Saved Successfully");
    }

    @DeleteMapping("/api/Cards/{CardId}")
    public void deleteCard(@PathVariable(name="CardId")Long CardId){
        CardService.deleteCard(CardId);
        System.out.println("Card Deleted Successfully");
    }

    @PutMapping("/api/Cards/{CardId}")
    public void updateCard(@RequestBody Card Card,
                               @PathVariable(name="CardId")Long CardId){
        Card emp = CardService.getCard(CardId);
        if(emp != null){
            CardService.updateCard(Card);
        }

    }

}


