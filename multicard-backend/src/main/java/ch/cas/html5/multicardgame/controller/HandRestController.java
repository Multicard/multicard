package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.implementation.HandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class HandRestController {
    @Autowired
    private HandServiceImpl handService;

    public void setGameService(HandServiceImpl handService) {
        this.handService = handService;
    }

//    @PostMapping("/api/Games/{PlaygroundId}")
//    public void startGame(@PathVariable(name="PlaygroundId")String playgroundId){
//        handService.startGame(playgroundId);
//    }


}


