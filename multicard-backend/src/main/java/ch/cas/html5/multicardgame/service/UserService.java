package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.User;

import java.util.List;


public interface UserService {

    public List<User> retrieveUsers();

    public User getUser(String userId);

    public User saveUser(User user);

    public void deleteUser(String userId);
}
