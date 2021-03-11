package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.UUID;

@Entity
@SequenceGenerator(name="action_seq", initialValue=1, allocationSize=10)
@Table(name="action")
public class Action {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @JsonIgnoreProperties("action")
    @ManyToOne(optional = true)
    private Game game;

    @JsonIgnoreProperties("action")
    @ManyToOne(optional = true)
    private Player player;

    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private long sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }
}
