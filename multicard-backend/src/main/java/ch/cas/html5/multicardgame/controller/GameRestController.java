package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.control.GameControlService;
import ch.cas.html5.multicardgame.dto.GameDTO;
import ch.cas.html5.multicardgame.dto.PlayerDTO;
import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.services.GameServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class GameRestController {
    private final GameServiceImpl gameService;
    private final GameControlService gameControlService;

    public GameRestController(GameServiceImpl gameService, GameControlService gameControlService) {
        this.gameService = gameService;
        this.gameControlService = gameControlService;
    }

    @GetMapping("/api/Games")
    public List<Game> getGames() {
        return gameService.retrieveGames();
    }

    @GetMapping("/api/Games/{GameId}")
    public GameDTO getGame(@PathVariable(name = "GameId") String gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return null;
        } else {
            GameDTO gamedto = new GameDTO(game.getId(), game.getTitle(), game.getState());
            for (Player p2 : game.getPlayers()) {
                PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition(), true);
                gamedto.getPlayers().add(playerdto);
            }
            return gamedto;
        }
    }

    @PostMapping(path = "/api/Games")
    public GameDTO saveGame(@RequestParam(name = "gameTitle") String title) {
        Game game =  gameService.saveGame(title);
        gameControlService.setGameReady(game);
        GameDTO gamedto = new GameDTO(game.getId(), game.getTitle(), game.getState());
        return gamedto;
    }

    @PutMapping(path = "/api/Games/{GameId}")
    public PlayerDTO addPlayer(@PathVariable(name = "GameId") String gameId,
                            @RequestHeader(name = "name") String name,
                            @RequestHeader(name = "isOrganizer") Boolean isOrganizer,
                            @RequestHeader(name = "position") int position,
                            @RequestHeader(name = "pwd") String pwd) {
        Player p2 = gameService.addPlayer(gameId, name, isOrganizer, position, pwd);
        PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition(), false);
        return playerdto;
    }
}


