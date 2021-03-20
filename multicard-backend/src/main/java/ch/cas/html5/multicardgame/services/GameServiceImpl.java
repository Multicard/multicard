package ch.cas.html5.multicardgame.services;


import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.repository.GameRepository;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl {
    @Autowired
    private GameRepository gameRepository;
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    private PlayerRepository playerRepository;
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public List<Game> retrieveGames() {
        List<Game> games = gameRepository.findAll();
        return games;
    }

    public Game getGame(String gameId) {
        Optional<Game> optGame = gameRepository.findById(gameId);
        if (optGame.isPresent()) {
            return optGame.get();
        }
        return null;
    }

    public Game saveGame(String title){
        Game game = new Game();
        game.setTitle(title);
        game.setState(Gamestate.INITIAL);
        return gameRepository.save(game);
    }

    public Game updateGame(Game game){
        return gameRepository.save(game);
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
            optGame.get().getPlayers().add(player);
            playerRepository.save(player);
            return  player;
        }
        return null;
    }


}
