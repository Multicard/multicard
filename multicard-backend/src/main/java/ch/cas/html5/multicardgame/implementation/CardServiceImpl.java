package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.repository.CardRepository;
import ch.cas.html5.multicardgame.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private ch.cas.html5.multicardgame.repository.CardRepository CardRepository;

    public void setCardRepository(CardRepository cardRepository) {
        this.CardRepository = cardRepository;
    }

    public List<Card> retrieveCards() {
        List<Card> cards = CardRepository.findAll();
        return cards;
    }

    public Card getCard(String cardId) {
        Optional<Card> optEmp = CardRepository.findById(cardId);
        return optEmp.get();
    }

    public Card saveCard(Card card){
        return CardRepository.save(card);
    }

    public void deleteCard(String cardId){
        CardRepository.deleteById(cardId);
    }

    public void updateCard(Card card) {
        CardRepository.save(card);
    }

}
