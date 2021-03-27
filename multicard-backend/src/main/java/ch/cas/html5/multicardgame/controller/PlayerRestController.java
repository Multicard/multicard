package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.dto.PlayerDTO;
import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.services.PlayerServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PlayerRestController {
    private final PlayerServiceImpl playerService;

    public PlayerRestController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/api/Players")
    public List<Player> getPlayers() {
        return playerService.retrievePlayers();
    }

    @GetMapping("/api/Players/{PlayerId}")
    public PlayerDTO getPlayer(@PathVariable(name="PlayerId") String playerId) {
        Player p2 =  playerService.getPlayer(playerId);
        PlayerDTO playerdto = new PlayerDTO(p2.getId(), p2.getName(), p2.getIsOrganizer(), p2.getPosition(), true);
        return playerdto;
    }

    @GetMapping("/api/Players/{PlayerId}/pwd")
    public Boolean checkPassword(@PathVariable(name="PlayerId") String playerId, @RequestHeader(name="pwd") String pwd) {
        return playerService.checkPassword(playerId, pwd);
    }

}


