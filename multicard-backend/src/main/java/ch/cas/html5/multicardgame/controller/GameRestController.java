package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.services.GameServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class GameRestController {
    private final GameServiceImpl gameService;

    public GameRestController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/Games")
    public List<Game> getGames() {
        return gameService.retrieveGames();
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


