package ch.cas.html5.multicardgame.controller;

import ch.cas.html5.multicardgame.entity.Playground;
import ch.cas.html5.multicardgame.entity.User;
import ch.cas.html5.multicardgame.service.PlaygroundService;
import ch.cas.html5.multicardgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class UserRestController {
    @Autowired
    private ch.cas.html5.multicardgame.service.UserService UserService;
    @Autowired
    private ch.cas.html5.multicardgame.service.PlaygroundService PlaygroundService;

    public void setUserService(UserService UserService) {
        this.UserService = UserService;
    }

    public void PlaygroundService(ch.cas.html5.multicardgame.service.PlaygroundService PlaygroundService) {
        this.PlaygroundService = PlaygroundService;
    }

    @GetMapping("/api/Users")
    public List<User> getUsers() {
        List<User> Users = UserService.retrieveUsers();
        return Users;
    }

    @GetMapping("/api/Users/{UserId}")
    public User getUser(@PathVariable(name="UserId")Long UserId) {
        return UserService.getUser(UserId);
    }

    @PostMapping(path = "/api/Users", consumes = "application/json", produces = "application/json")
    public User saveUser(@RequestBody User user){
        return UserService.saveUser(user);
    }

    @DeleteMapping("/api/Users/{UserId}")
    public void deleteUser(@PathVariable(name="UserId")Long UserId){
        UserService.deleteUser(UserId);
        System.out.println("User Deleted Successfully");
    }

    @PutMapping("/api/Users/{UserId}")
    public User updateUser(@RequestBody User user,
                               @PathVariable(name="UserId")Long UserId){

/*
        Playground p = PlaygroundService.getPlayground(user.getPlayground().getId());
        p.addPlayer(user);
        PlaygroundService.savePlayground(p);
*/

        User source = UserService.getUser(UserId);
        source.setPlayground(user.getPlayground());
        if(source != null){
            UserService.saveUser(user);
        }
        return user;
    }

}


