package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.entity.PlayedCard;
import ch.cas.html5.multicardgame.repository.CardRepository;
import ch.cas.html5.multicardgame.repository.PlayedCardRepository;
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

    @Autowired
    private PlayedCardRepository playedCardRepository;

    public void setPlayedCardRepository(PlayedCardRepository playedCardRepository) {
        this.playedCardRepository = playedCardRepository;
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

    public void deleteCard(Card card){
        cardRepository.delete(card);
//        cardRepository.deleteById(cardId);
    }

    public void updateCard(Card card) {
        cardRepository.save(card);
    }

    public PlayedCard savePlayedCard(PlayedCard playedCard){
        return playedCardRepository.save(playedCard);
    }

    public void deletePlayedCard(String playedCardId){
        playedCardRepository.deleteById(playedCardId);
    }
}
