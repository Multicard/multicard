package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.repository.PlayedCardsRepository;
import org.springframework.stereotype.Service;

@Service
public class PLayedCardsServiceImpl {

    private final PlayedCardsRepository pLayedCardsRepository;

    public PLayedCardsServiceImpl(PlayedCardsRepository pLayedCardsRepository) {
        this.pLayedCardsRepository = pLayedCardsRepository;
    }

    public void deletePlayedCards(String playedCardsId) {
        pLayedCardsRepository.deleteById(playedCardsId);
    }

}

