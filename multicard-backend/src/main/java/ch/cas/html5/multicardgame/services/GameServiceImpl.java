package ch.cas.html5.multicardgame.services;


import ch.cas.html5.multicardgame.entity.*;
import ch.cas.html5.multicardgame.control.GameControlService;
import ch.cas.html5.multicardgame.enums.Gamestate;
import ch.cas.html5.multicardgame.repository.GameRepository;
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
    private GameControlService gameControlService;
    public void setGameControlService(GameControlService gameControlService) {
        this.gameControlService = gameControlService;
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

    public void deleteGame(String gameId){
        gameRepository.deleteById(gameId);
    }


}
