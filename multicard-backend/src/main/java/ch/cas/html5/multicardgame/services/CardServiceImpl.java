package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CardServiceImpl {
    @Autowired
    private CardRepository cardRepository;

    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> retrieveCards() {
        List<Card> cards = cardRepository.findAll();
        return cards;
    }

    public Card getCard(String cardId) {
        Optional<Card> optEmp = cardRepository.findById(cardId);
        return optEmp.get();
    }

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }

    public void deleteCard(String cardId){
        cardRepository.deleteById(cardId);
    }

    public void updateCard(Card card) {
        cardRepository.save(card);
    }

}
