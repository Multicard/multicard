package ch.cas.html5.multicardgame.service;

import ch.cas.html5.multicardgame.entity.Game;

import java.util.List;

public interface GameService {

    public List<Game> retrieveGames();

    public Game getGame(String gameId);

    public Game saveGame(Game game);

    public void deleteGame(String gameId);

    public void updateGame(Game game);
}
