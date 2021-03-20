package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.services.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class CardRestController {
    @Autowired
    private CardServiceImpl cardService;

    public void setCardService(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

//    @GetMapping("/api/Cards")
//    public List<Card> getCards() {
//        List<Card> cards = cardService.retrieveCards();
//        return cards;
//    }
//
//    @GetMapping("/api/Cards/{CardId}")
//    public Card getCard(@PathVariable(name="CardId")String cardId) {
//        return cardService.getCard(cardId);
//    }
//
//    @PostMapping("/api/Cards")
//    public void saveCard(Card card){
//        cardService.saveCard(card);
//    }
//
//    @PutMapping("/api/Cards/{CardId}")
//    public void updateCard(@RequestBody Card card,
//                               @PathVariable(name="CardId")String cardId){
//        Card emp = cardService.getCard(cardId);
//        if(emp != null){
//            cardService.updateCard(card);
//        }
//
//    }

}


