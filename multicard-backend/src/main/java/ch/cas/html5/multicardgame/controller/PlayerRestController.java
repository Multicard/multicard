package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.services.PlayerServiceImpl;
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

    @GetMapping("/api/Players/{PlayerId}/{pwd}")
    public Boolean checkPassword(@PathVariable(name="PlayerId")String playerId, @PathVariable(name="pwd")String pwd) {
        return playerService.checkPassword(playerId, pwd);
    }

}


