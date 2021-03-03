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

    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> retrievePlayers() {
        List<Player> players = playerRepository.findAll();
        return players;
    }

    public Player getPlayer(String userId) {
        Optional<Player> optPlayer = playerRepository.findById(userId);
        if (optPlayer.isPresent()){
            return optPlayer.get();
        }
        return null;
    }

    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }

    public void deletePlayer(String userId){
        playerRepository.deleteById(userId);
    }

    public List<Player> getUsesersByPlayground(String playgroundId){
        List<Player> players = playerRepository.getPlayersByGame(playgroundId);
        return players;
    }


}
