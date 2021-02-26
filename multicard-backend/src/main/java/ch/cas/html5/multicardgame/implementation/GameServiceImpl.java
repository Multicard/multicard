package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Card;
import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.User;
import ch.cas.html5.multicardgame.repository.CardRepository;
import ch.cas.html5.multicardgame.repository.UserRepository;
import ch.cas.html5.multicardgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private ch.cas.html5.multicardgame.repository.GameRepository gameRepository;

    public void setGameRepository(ch.cas.html5.multicardgame.repository.GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private CardRepository cardRepository;

    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Game> retrieveGames() {
        List<Game> games = gameRepository.findAll();
        return games;
    }

    public Game getGame(String gameId) {
        Optional<Game> optEmp = gameRepository.findById(gameId);
        return optEmp.get();
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(String gameId) {
        gameRepository.deleteById(gameId);
    }

    public void updateGame(Game game) {
        gameRepository.save(game);
    }

    public void startGame(String playgroundId) {
        List<User> users = deleteGameByPlayground(playgroundId);
        handOutCards(playgroundId, users);
    }

    private List<User> deleteGameByPlayground(String playgroundId) {
        List<User> users = userRepository.getUserByPlayground(playgroundId);
        for (User user : users) {
            gameRepository.deleteGameByUser(user.getId());
        }
        return users;
    }

    private void handOutCards(String playgroundId, List<User> users) {
        final List<Card> cards = cardRepository.findAll();
        final List<Integer> indexUser = Arrays.asList(0, 1, 2, 3);
        int cnt = 36; //random 0 included bound exclusive
        for (int i = 0; i <= 35; i = i + 1) {
            int random = generateRandomInt(cnt);
            Card card = cards.get(random);
            //System.out.println(i+1 + " - zufällige Karte: " + card.getName());
            cards.remove(random);
            cnt = cnt - 1;
            Collections.rotate(indexUser, 1);
            Game game = new Game();
            game.setCard(card);
            game.setUser(users.get(indexUser.get(0)));
            System.out.println("Save Game " + card.getName() + " - " + game.getUser().getId());
            gameRepository.save(game);
        }

    }

    private Integer generateRandomInt(int bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
}
