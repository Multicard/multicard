package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Hand;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.messaging.WebSocketController;
import ch.cas.html5.multicardgame.repository.CardRepository;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HandServiceImpl {
    @Autowired
    private ch.cas.html5.multicardgame.repository.HandRepository handRepository;

    public void setGameRepository(ch.cas.html5.multicardgame.repository.HandRepository handRepository) {
        this.handRepository = handRepository;
    }

    @Autowired
    private PlayerRepository playerRepository;

    public void setUserRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    private CardRepository cardRepository;

    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Autowired
    private WebSocketController webSocketController;
    public void setWebSocketController(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }



    public List<Hand> retrieveGames() {
        List<Hand> hands = handRepository.findAll();
        return hands;
    }


    public Hand getGame(String gameId) {
        Optional<Hand> optEmp = handRepository.findById(gameId);
        return optEmp.get();
    }

    public Hand saveHand(Hand hand) {
        return handRepository.save(hand);
    }

    public void deleteGame(String gameId) {
        handRepository.deleteById(gameId);
    }

    public void updateHand(Hand hand) {
        handRepository.save(hand);
    }

    private List<Player> deleteHandByGame(String gameId) {
        List<Player> players = playerRepository.getPlayersByGame(gameId);
        for (Player player : players) {
            handRepository.deleteGameByPLayer(player.getId());
        }
        return players;
    }

}
