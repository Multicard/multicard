package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CardServiceImpl {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getCard(String cardId) {
        Optional<Card> optEmp = cardRepository.findById(cardId);
        return optEmp.orElse(null);
    }

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }

    public void deleteCard(Card card){
        cardRepository.delete(card);
    }

}
