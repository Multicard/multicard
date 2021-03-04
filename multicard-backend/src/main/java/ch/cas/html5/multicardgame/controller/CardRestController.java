package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.implementation.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CardRestController {
    @Autowired
    private CardServiceImpl cardService;

    public void setCardService(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/api/Cards")
    public List<Card> getCards() {
        List<Card> cards = cardService.retrieveCards();
        return cards;
    }

    @GetMapping("/api/Cards/{CardId}")
    public Card getCard(@PathVariable(name="CardId")String cardId) {
        return cardService.getCard(cardId);
    }

    @PostMapping("/api/Cards")
    public void saveCard(Card card){
        cardService.saveCard(card);
    }

    @DeleteMapping("/api/Cards/{CardId}")
    public void deleteCard(@PathVariable(name="CardId")String cardId){
        cardService.deleteCard(cardId);
    }

    @PutMapping("/api/Cards/{CardId}")
    public void updateCard(@RequestBody Card card,
                               @PathVariable(name="CardId")String cardId){
        Card emp = cardService.getCard(cardId);
        if(emp != null){
            cardService.updateCard(card);
        }

    }

}


