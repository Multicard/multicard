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

    public void setHandRepository(ch.cas.html5.multicardgame.repository.HandRepository handRepository) {
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


    public Hand getHand(String handId) {
        Optional<Hand> optEmp = handRepository.findById(handId);
        return optEmp.get();
    }

    public Hand saveHand(Hand hand) {
        return handRepository.save(hand);
    }

    public void deleteHand(String handId) {
        handRepository.deleteById(handId);
    }

    public void updateHand(Hand hand) {
        handRepository.save(hand);
    }

    public List<Player> deleteHandByGame(String gameId) {
        List<Player> players = playerRepository.getPlayersByGame(gameId);
        for (Player player : players) {
            handRepository.deleteHandByPLayer(player.getId());
        }
        return players;
    }

}
