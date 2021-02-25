package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Playground;
import ch.cas.html5.multicardgame.entity.User;
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
    private ch.cas.html5.multicardgame.service.UserService UserService;

    public void setPlaygroundService(PlaygroundService PlaygroundService) {
        this.PlaygroundService = PlaygroundService;
    }

    @GetMapping("/api/Playgrounds")
    public List<Playground> getPlaygrounds() {
        List<Playground> Playgrounds = PlaygroundService.retrievePlaygrounds();
        return Playgrounds;
    }

    @GetMapping("/api/Playgrounds/{PlaygroundId}")
    public Playground getPlayground(@PathVariable(name="PlaygroundId")String PlaygroundId) {
        return PlaygroundService.getPlayground(PlaygroundId);
    }

    @PostMapping(path = "/api/Playgrounds", consumes = "application/json", produces = "application/json")
    public Playground savePlayground(@RequestBody Playground playground){
        return PlaygroundService.savePlayground(playground);
    }

    @DeleteMapping("/api/Playgrounds/{PlaygroundId}")
    public void deletePlayground(@PathVariable(name="PlaygroundId")String PlaygroundId){
        PlaygroundService.deletePlayground(PlaygroundId);
        System.out.println("Playground Deleted Successfully");
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


