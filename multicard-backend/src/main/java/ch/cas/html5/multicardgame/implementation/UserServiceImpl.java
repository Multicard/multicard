package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.User;
import ch.cas.html5.multicardgame.repository.UserRepository;
import ch.cas.html5.multicardgame.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private ch.cas.html5.multicardgame.repository.UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> retrieveUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUser(String userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()){
            return optUser.get();
        }
        return null;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    public List<User> getUsesersByPlayground(String playgroundId){
        List<User> users = userRepository.getUserByPlayground(playgroundId);
        return users;
    }


}
