package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class GameRestController {
    @Autowired
    private GameService gameService;

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/api/Games/{PlaygroundId}")
    public void startGame(@PathVariable(name="PlaygroundId")String playgroundId){
        gameService.startGame(playgroundId);
    }


}


