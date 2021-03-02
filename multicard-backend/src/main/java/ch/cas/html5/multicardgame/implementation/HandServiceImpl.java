package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Hand;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.messaging.WebSocketController;
import ch.cas.html5.multicardgame.repository.CardRepository;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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

//    public void startGame(String gameId) {
//        List<Player> players = deleteGameByPlayground(gameId);
//        handOutCards(gameId, players);
//        //webSocketController.sendToUser("933F4FB5-5144-4932-AF0A-C2098E24D184", "103F5A8A-7A5B-49F9-89E2-81D58183ED2E");
//    }

    private List<Player> deleteHandByGame(String gameId) {
        List<Player> players = playerRepository.getUserByGame(gameId);
        for (Player player : players) {
            handRepository.deleteGameByPLayer(player.getId());
        }
        return players;
    }

//    private void handOutCards(String playgroundId, List<Player> players) {
//        final List<Card> cards = cardRepository.findAll();
//        final List<Integer> indexUser = Arrays.asList(0, 1, 2, 3);
//        int cnt = 36; //random 0 included bound exclusive
//        for (int i = 0; i <= 35; i = i + 1) {
//            int random = generateRandomInt(cnt);
//            Card card = cards.get(random);
//            //System.out.println(i+1 + " - zufällige Karte: " + card.getName());
//            cards.remove(random);
//            cnt = cnt - 1;
//            Collections.rotate(indexUser, 1);
//            Hand hand = new Hand();
//            hand.setCard(card);
//            hand.setPlayer(players.get(indexUser.get(0)));
//            System.out.println("Save Game " + card.getName() + " - " + hand.getPlayer().getId());
//            handRepository.save(hand);
//            Message msg = new Message();
//            msg.setFrom("Server");
//            msg.setText("Karte: " + card.getName());
//            webSocketController.sendToUser(playgroundId, players.get(indexUser.get(0)).getId(), msg);
//
//        }
//
//    }

    private Integer generateRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
}
