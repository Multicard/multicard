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
    private ch.cas.html5.multicardgame.repository.UserRepository UserRepository;

    public void setUserRepository(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    public List<User> retrieveUsers() {
        List<User> Users = UserRepository.findAll();
        return Users;
    }

    public User getUser(Long UserId) {
        Optional<User> optUser = UserRepository.findById(UserId);
        if (optUser.isPresent()){
            return optUser.get();
        }
        return null;
    }

    public User saveUser(User User){
        return UserRepository.save(User);
    }

    public void deleteUser(Long UserId){
        UserRepository.deleteById(UserId);
    }


}
