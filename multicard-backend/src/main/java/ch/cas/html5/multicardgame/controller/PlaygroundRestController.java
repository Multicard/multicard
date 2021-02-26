package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Playground;
import ch.cas.html5.multicardgame.service.PlaygroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class PlaygroundRestController {
    @Autowired
    private ch.cas.html5.multicardgame.service.PlaygroundService PlaygroundService;
    @Autowired
    private ch.cas.html5.multicardgame.service.UserService userService;

    public void setPlaygroundService(PlaygroundService playgroundService) {
        this.PlaygroundService = playgroundService;
    }

    @GetMapping("/api/Playgrounds")
    public List<Playground> getPlaygrounds() {
        List<Playground> playgrounds = PlaygroundService.retrievePlaygrounds();
        return playgrounds;
    }

    @GetMapping("/api/Playgrounds/{PlaygroundId}")
    public Playground getPlayground(@PathVariable(name="PlaygroundId")String playgroundId) {
        return PlaygroundService.getPlayground(playgroundId);
    }

    @PostMapping(path = "/api/Playgrounds", consumes = "application/json", produces = "application/json")
    public Playground savePlayground(@RequestBody Playground playground){
        return PlaygroundService.savePlayground(playground);
    }

    @DeleteMapping("/api/Playgrounds/{PlaygroundId}")
    public void deletePlayground(@PathVariable(name="PlaygroundId")String playgroundId){
        PlaygroundService.deletePlayground(playgroundId);
    }

    @PutMapping("/api/Playgrounds/{PlaygroundId}")
    public Playground updatePlayground(@RequestBody Playground playground,
                               @PathVariable(name="PlaygroundId")String playgroundId){
        Playground updatePlayground = PlaygroundService.getPlayground(playgroundId);
        if (updatePlayground != null){
            if (updatePlayground.getPlayers() == null){
                updatePlayground.setPlayers(new ArrayList<>());
            }
            updatePlayground.addPlayer(playground.getPlayers().get(0));
            PlaygroundService.savePlayground(updatePlayground);
        }
        return updatePlayground;
    }

}


