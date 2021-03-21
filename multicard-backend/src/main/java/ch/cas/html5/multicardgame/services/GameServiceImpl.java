package ch.cas.html5.multicardgame.services;


import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.repository.GameRepository;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public List<Game> retrieveGames() {
        return gameRepository.findAll();
    }

    public Game getGame(String gameId) {
        Optional<Game> optGame = gameRepository.findById(gameId);
        return optGame.orElse(null);
    }

    public Game saveGame(String title){
        Game game = new Game();
        game.setTitle(title);
        game.setState(Gamestate.INITIAL);
        return gameRepository.save(game);
    }

    public void updateGame(Game game){
        gameRepository.save(game);
    }

    public Player addPlayer(String gameId, String name, Boolean isOrganizer, int position, String pwd){
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            Player player = new Player();
            player.setName(name);
            player.setIsOrganizer(isOrganizer);
            player.setPosition(position);
            player.setPwd(pwd);
            player.setGame(optGame.get());
            playerRepository.save(player);
            optGame.get().getPlayers().add(player);
            return  player;
        }
        return null;
    }


}
