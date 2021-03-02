package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PlayerServiceImpl {
    @Autowired
    private ch.cas.html5.multicardgame.repository.PlayerRepository playerRepository;

    public void setUserRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> retrieveUsers() {
        List<Player> players = playerRepository.findAll();
        return players;
    }

    public Player getUser(String userId) {
        Optional<Player> optUser = playerRepository.findById(userId);
        if (optUser.isPresent()){
            return optUser.get();
        }
        return null;
    }

    public Player saveUser(Player player){
        return playerRepository.save(player);
    }

    public void deleteUser(String userId){
        playerRepository.deleteById(userId);
    }

    public List<Player> getUsesersByPlayground(String playgroundId){
        List<Player> players = playerRepository.getUserByGame(playgroundId);
        return players;
    }


}
