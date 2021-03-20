package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.control.GameControlService;
import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.services.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class GameRestController {
    @Autowired
    private GameServiceImpl gameService;
    public void setGameService(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @Autowired
    private GameControlService gameControlService;
    public void setGameControlService(GameControlService gameControlService) {
        this.gameControlService = gameControlService;
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
    public Game saveGame(@RequestBody String title){
        return gameService.saveGame(title);
    }

    @PutMapping(path = "/api/Games", consumes = "application/json", produces = "application/json")
    public Player addPlayer(@RequestBody String gameId, String name, Boolean isOrganizer, int position, String pwd){
        return gameService.addPlayer(gameId, name, isOrganizer, position, pwd);
    }
}


