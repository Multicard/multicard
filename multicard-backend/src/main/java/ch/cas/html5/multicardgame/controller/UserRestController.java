package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.User;
import ch.cas.html5.multicardgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class UserRestController {
    @Autowired
    private ch.cas.html5.multicardgame.service.UserService userService;
    @Autowired
    private ch.cas.html5.multicardgame.service.PlaygroundService playgroundService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPlaygroundService(ch.cas.html5.multicardgame.service.PlaygroundService playgroundService) {
        this.playgroundService = playgroundService;
    }

    @GetMapping("/api/Users")
    public List<User> getUsers() {
        List<User> users = userService.retrieveUsers();
        return users;
    }

    @GetMapping("/api/Users/{UserId}")
    public User getUser(@PathVariable(name="UserId")String userId) {
        return userService.getUser(userId);
    }

    @PostMapping(path = "/api/Users", consumes = "application/json", produces = "application/json")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @DeleteMapping("/api/Users/{UserId}")
    public void deleteUser(@PathVariable(name="UserId")String userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/api/Users/{UserId}")
    public User updateUser(@RequestBody User user,
                               @PathVariable(name="UserId")String userId){

/*
        Playground p = PlaygroundService.getPlayground(user.getPlayground().getId());
        p.addPlayer(user);
        PlaygroundService.savePlayground(p);
*/

        User source = userService.getUser(userId);
        source.setPlayground(user.getPlayground());
        if(source != null){
            userService.saveUser(user);
        }
        return user;
    }

}


