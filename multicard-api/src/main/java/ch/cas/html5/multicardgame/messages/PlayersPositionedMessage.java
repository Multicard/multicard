package ch.cas.html5.multicardgame.messages;

import ch.cas.html5.multicardgame.dto.PlayerDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayersPositionedMessage extends GameMessage{
    private List<PlayerDTO> players = new ArrayList<>();

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
