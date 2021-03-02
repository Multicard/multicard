package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.implementation.GameServiceImpl;
import ch.cas.html5.multicardgame.implementation.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class GameRestController {
    @Autowired
    private GameServiceImpl gameService;
    @Autowired
    private PlayerServiceImpl playerService;

    public void setGameService(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/Games")
    public List<Game> getGames() {
        List<Game> games = gameService.retrieveGames();
        return games;
    }

    @GetMapping("/api/Games/{GameId}")
    public Game getGame(@PathVariable(name="GameId")String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping(path = "/api/Games", consumes = "application/json", produces = "application/json")
    public Game saveGame(@RequestBody Game game){
        return gameService.saveGame(game);
    }

    @DeleteMapping("/api/Games/{GameId}")
    public void deleteGame(@PathVariable(name="GameId")String gameId){
        gameService.deleteGame(gameId);
    }

    @PutMapping("/api/Games/{GameId}")
    public Game updateGame(@RequestBody Game game,
                               @PathVariable(name="GameId")String gameId){
        Game updateGame = gameService.getGame(gameId);
        if (updateGame != null){
            if (updateGame.getPlayers() == null){
                updateGame.setPlayers(new ArrayList<>());
            }
            updateGame.addPlayer(game.getPlayers().get(0));
            gameService.saveGame(updateGame);
        }
        return updateGame;
    }

}


