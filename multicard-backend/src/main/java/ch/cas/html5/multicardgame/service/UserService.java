package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.User;

import java.util.List;


public interface UserService {

    public List<User> retrieveUsers();

    public User getUser(Long UserId);

    public User saveUser(User User);

    public void deleteUser(Long UserId);
}
