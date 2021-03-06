package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="game")
public class Game {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name="title")
    private String title;

    //https://stackoverflow.com/questions/49130173/how-to-fix-spring-boot-one-to-many-bidirectional-infinity-loop
    @JsonIgnoreProperties("game")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players = new ArrayList<>();

    @Basic
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private Gamestate state;

    @JsonIgnoreProperties("game")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Stack> stacks = new HashSet<>();

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
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
}
