package ch.cas.html5.multicardgame.services;

import ch.cas.html5.multicardgame.entity.Player;
import ch.cas.html5.multicardgame.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class PlayerServiceImpl {
    private final ch.cas.html5.multicardgame.repository.PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> retrievePlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayer(String playerId) {
        Optional<Player> optPlayer = playerRepository.findById(playerId);
        return optPlayer.orElse(null);
    }

    public Boolean checkPassword(String playerId, String pwd) {
        Optional<Player> optPlayer = playerRepository.findById(playerId);
        return optPlayer.filter(player -> Objects.equals(player.getPwd(), pwd)).isPresent();
    }


    public void savePlayer(Player player){
        playerRepository.save(player);
    }


}
