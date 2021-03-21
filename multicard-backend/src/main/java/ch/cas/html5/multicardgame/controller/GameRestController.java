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
    public Game getGame(@PathVariable(name = "GameId") String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping(path = "/api/Games", consumes = "application/json", produces = "application/json")
    public Game saveGame(@RequestParam(name = "gameTitle") String title) {
        return gameService.saveGame(title);
    }

    @PutMapping(path = "/api/Games/{GameId}", consumes = "application/json", produces = "application/json")
    public Player addPlayer(@PathVariable(name = "GameId") String gameId,
                            @RequestHeader(name = "name") String name,
                            @RequestHeader(name = "isOrganizer") Boolean isOrganizer,
                            @RequestHeader(name = "position") int position,
                            @RequestHeader(name = "pwd") String pwd) {
        return gameService.addPlayer(gameId, name, isOrganizer, position, pwd);
    }
}


