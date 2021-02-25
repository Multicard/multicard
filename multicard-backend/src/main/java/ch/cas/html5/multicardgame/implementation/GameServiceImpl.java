package ch.cas.html5.multicardgame.implementation;

import ch.cas.html5.multicardgame.entity.Game;
import ch.cas.html5.multicardgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private ch.cas.html5.multicardgame.repository.GameRepository gameRepository;

    public void setGameRepository(ch.cas.html5.multicardgame.repository.GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> retrieveGames() {
        List<Game> games = gameRepository.findAll();
        return games;
    }

    public Game getGame(String gameId) {
        Optional<Game> optEmp = gameRepository.findById(gameId);
        return optEmp.get();
    }

    public Game saveGame(Game game){
        return gameRepository.save(game);
    }

    public void deleteGame(String gameId){
        gameRepository.deleteById(gameId);
    }

    public void updateGame(Game game) {
        gameRepository.save(game);
    }

}
