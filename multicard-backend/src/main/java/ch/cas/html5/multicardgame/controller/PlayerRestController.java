package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.implementation.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PlayerRestController {
    @Autowired
    private PlayerServiceImpl playerService;
    public void setPlayerService(PlayerServiceImpl playerService) {
        this.playerService = playerService;
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
    public Player savePlayer(@RequestBody String gameId, String name, Boolean isOrganizer, int position){
        return playerService.savePlayer(gameId, name, isOrganizer, position);
    }

//    @PostMapping(path = "/api/Players", consumes = "application/json", produces = "application/json")
//    public Player savePlayer(@RequestBody Player player){
//        return playerService.savePlayer(player);
//    }

    @DeleteMapping("/api/Players/{PlayerId}")
    public void deletePlayer(@PathVariable(name="PlayerId")String playerId){
        playerService.deletePlayer(playerId);
    }

    @PutMapping("/api/Players/{PlayerId}")
    public Player updatePlayer(@RequestBody String name, Boolean isOrganizer, int position,
                             @PathVariable(name="PlayerId")String playerId){
        return playerService.updatePlayer(playerId, name, isOrganizer, position);
    }

}


