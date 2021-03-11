package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.PlayedCards;
import ch.cas.html5.multicardgame.repository.PlayedCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PLayedCardsServiceImpl {

    @Autowired
    private PlayedCardsRepository pLayedCardsRepository;

    public void setPLayedCardsRepository(PlayedCardsRepository pLayedCardsRepository) {
        this.pLayedCardsRepository = pLayedCardsRepository;
    }

    public List<PlayedCards> retrievePLayedCards() {
        List<PlayedCards> playedCards = pLayedCardsRepository.findAll();
        return playedCards;
    }

    public PlayedCards getPlayedCards(String playedCardsId) {
        Optional<PlayedCards> optEmp = pLayedCardsRepository.findById(playedCardsId);
        return optEmp.get();
    }

    public PlayedCards savePlayedCards(PlayedCards playedCards) {
        return pLayedCardsRepository.save(playedCards);
    }

    public void deletePlayedCards(String playedCardsId) {
        pLayedCardsRepository.deleteById(playedCardsId);
    }

    public void updatePlayedCards(PlayedCards playedCards) {
        pLayedCardsRepository.save(playedCards);
    }

}

