package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Game;
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

    public Player savePlayer(String gameId, String name, Boolean isOrganizer, int position){

        Game game = gameService.getGame(gameId);
        if (game != null){
            Player player = new Player();
            player.setGame(game);
            player.setName(name);
            player.setIsOrganizer(isOrganizer);
            player.setPosition(position);
            game.addPlayer(player);
            return player;
        }
        return null;
    }

//    public Player savePlayer(Player player){
//        return playerRepository.save(player);
//    }

    public void deletePlayer(String playerId){
        playerRepository.deleteById(playerId);
    }

    public List<Player> getUsesersByPlayground(String gameId){
        List<Player> players = playerRepository.getPlayersByGame(gameId);
        return players;
    }

    public Player updatePlayer(String playerId, String name, Boolean isOrganizer, int position){
        Player player = getPlayer(playerId);
        if (player == null) {
            return null;
        }
        player.setName(name);
        player.setIsOrganizer(isOrganizer);
        player.setPosition(position);
        return playerRepository.save(player);
    }

}
