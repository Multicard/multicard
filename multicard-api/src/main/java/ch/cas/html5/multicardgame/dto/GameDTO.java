package ch.cas.html5.multicardgame.dto;

import ch.cas.html5.multicardgame.enums.Gamestate;

import java.util.ArrayList;
import java.util.List;

public class GameDTO {
    private String id;
    private String title;
    private Gamestate state;
    private List<PlayerDTO> players = new ArrayList<>();
    private List<StackDTO> stacks = new ArrayList<>();
    private PlayedCardsDTO playedCards;
    private ActionDTO lastAction;
    private Integer currentRound;
    private List<ScoreDTO> scores = new ArrayList<>();

    public GameDTO(String id, String title, Gamestate state){
        this.id = id;
        this.title = title;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public List<StackDTO> getStacks() {
        return stacks;
    }

    public void setStacks(List<StackDTO> stacks) {
        this.stacks = stacks;
    }

    public Gamestate getState() {
        return state;
    }

    public void setState(Gamestate state) {
        this.state = state;
    }

    public PlayedCardsDTO getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(PlayedCardsDTO playedCards) {
        this.playedCards = playedCards;
    }

    public ActionDTO getLastAction() {
        return lastAction;
    }

    public void setLastAction(ActionDTO lastAction) {
        this.lastAction = lastAction;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public List<ScoreDTO> getScores() {
        return scores;
    }

    public void setScores(List<ScoreDTO> scores) {
        this.scores = scores;
    }
}
