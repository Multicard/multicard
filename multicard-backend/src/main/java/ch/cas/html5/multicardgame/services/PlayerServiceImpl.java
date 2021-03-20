package ch.cas.html5.multicardgame.services;

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

    @Autowired
    private GameServiceImpl gameService;
    public void setGameService(GameServiceImpl gameService) {
        this.gameService = gameService;
    }


    public List<Player> retrievePlayers() {
        List<Player> players = playerRepository.findAll();
        return players;
    }

    public Player getPlayer(String playerId) {
        Optional<Player> optPlayer = playerRepository.findById(playerId);
        if (optPlayer.isPresent()){
            return optPlayer.get();
        }
        return null;
    }

    public Boolean checkPassword(String playerId, String pwd) {
        Optional<Player> optPlayer = playerRepository.findById(playerId);
        if (optPlayer.isPresent()){
            if (optPlayer.get().getPwd().equals(pwd)){
                return true;
            }
        }
        return false;
    }


    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }


}
