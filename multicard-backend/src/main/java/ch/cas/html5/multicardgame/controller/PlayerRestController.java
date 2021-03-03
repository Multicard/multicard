package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.implementation.GameServiceImpl;
import ch.cas.html5.multicardgame.implementation.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PlayerRestController {
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private GameServiceImpl gameService;

    public void setPlayerService(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    public void setPlaygroundService(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/Players")
    public List<Player> getPlayers() {
        List<Player> players = playerService.retrievePlayers();
        return players;
    }

    @GetMapping("/api/Players/{PlayerId}")
    public Player getPlayer(@PathVariable(name="PlayerId")String playerId) {
        return playerService.getPlayer(playerId);
    }

    @PostMapping(path = "/api/Players", consumes = "application/json", produces = "application/json")
    public Player savePlayer(@RequestBody Player player){
        return playerService.savePlayer(player);
    }

    @DeleteMapping("/api/Players/{PlayerId}")
    public void deletePlayer(@PathVariable(name="PlayerId")String playerId){
        playerService.deletePlayer(playerId);
    }

    @PutMapping("/api/Players/{PlayerId}")
    public Player updatePlayer(@RequestBody Player player,
                             @PathVariable(name="PlayerId")String playerId){

/*
        Playground p = PlaygroundService.getPlayground(player.getPlayground().getId());
        p.addPlayer(player);
        PlaygroundService.savePlayground(p);
*/

        Player source = playerService.getPlayer(playerId);
        source.setGame(player.getGame());
        if(source != null){
            playerService.savePlayer(player);
        }
        return player;
    }

}


