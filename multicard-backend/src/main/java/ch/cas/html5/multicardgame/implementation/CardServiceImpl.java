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

    public void setCardRepository(CardRepository CardRepository) {
        this.CardRepository = CardRepository;
    }

    public List<Card> retrieveCards() {
        List<Card> Cards = CardRepository.findAll();
        return Cards;
    }

    public Card getCard(Long CardId) {
        Optional<Card> optEmp = CardRepository.findById(CardId);
        return optEmp.get();
    }

    public void saveCard(Card Card){
        CardRepository.save(Card);
    }

    public void deleteCard(Long CardId){
        CardRepository.deleteById(CardId);
    }

    public void updateCard(Card Card) {
        CardRepository.save(Card);
    }

}
