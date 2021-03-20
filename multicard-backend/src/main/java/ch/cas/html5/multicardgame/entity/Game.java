package ch.cas.html5.multicardgame.entity;

import ch.cas.html5.multicardgame.enums.Gamestate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="game")
public class Game {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name="title")
    private String title;

    //https://stackoverflow.com/questions/49130173/how-to-fix-spring-boot-one-to-many-bidirectional-infinity-loop
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Player> players = new HashSet<>();

    @Basic
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private Gamestate state;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stack> stacks = new HashSet<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "playedcards_id", referencedColumnName = "id")
    private PlayedCards playedcards;

    @JsonIgnore
    @JsonIgnoreProperties("action")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Action> actions = new HashSet<>();

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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setGame(this);
    }

    public Gamestate getState() { return state; }

    public void setState(Gamestate state) {this.state = state;  }

    public Set<Stack> getGameStacks() {
        return stacks;
    }

    public void setGameStacks(Set<Stack> stacks) {
        this.stacks = stacks;
    }

    public PlayedCards getPlayedcards() {
        return playedcards;
    }

    public void setPlayedcards(PlayedCards playedCards) {
        this.playedcards = playedCards;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }
}
