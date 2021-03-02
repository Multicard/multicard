package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.implementation.GameServiceImpl;
import ch.cas.html5.multicardgame.implementation.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class UserRestController {
    @Autowired
    private PlayerServiceImpl playerService;
    @Autowired
    private GameServiceImpl gameService;

    public void setUserService(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    public void setGameService(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/api/Users")
    public List<Player> getUsers() {
        List<Player> players = playerService.retrieveUsers();
        return players;
    }

    @GetMapping("/api/Users/{UserId}")
    public Player getUser(@PathVariable(name="UserId")String userId) {
        return playerService.getUser(userId);
    }

    @PostMapping(path = "/api/Users", consumes = "application/json", produces = "application/json")
    public Player saveUser(@RequestBody Player player){
        return playerService.saveUser(player);
    }

    @DeleteMapping("/api/Users/{UserId}")
    public void deleteUser(@PathVariable(name="UserId")String userId){
        playerService.deleteUser(userId);
    }

    @PutMapping("/api/Users/{UserId}")
    public Player updateUser(@RequestBody Player player,
                             @PathVariable(name="UserId")String userId){

/*
        Playground p = PlaygroundService.getPlayground(user.getPlayground().getId());
        p.addPlayer(user);
        PlaygroundService.savePlayground(p);
*/

        Player source = playerService.getUser(userId);
        source.setGame(player.getGame());
        if(source != null){
            playerService.saveUser(player);
        }
        return player;
    }

}


