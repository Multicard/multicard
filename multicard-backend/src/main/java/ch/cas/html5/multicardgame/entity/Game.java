package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
    @JsonIgnoreProperties("game")
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @Basic
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private Gamestate state;

    @JsonIgnoreProperties("game")
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stacks = new ArrayList<>();

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

    public List<Stack> getGameStacks() {
        return stacks;
    }

    public void setGameStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }
}
