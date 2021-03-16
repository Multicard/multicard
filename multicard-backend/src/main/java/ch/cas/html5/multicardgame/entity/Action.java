package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
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

    @Column(name="action")
    private ch.cas.html5.multicardgame.enums.Action action;

    @Column(name="sort")
    private Long sort;

    @ElementCollection
    @Column(name="card_id")
    private Set<String> cards_id = new HashSet<>();

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

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public ch.cas.html5.multicardgame.enums.Action getAction() {
        return action;
    }

    public void setAction(ch.cas.html5.multicardgame.enums.Action action) {
        this.action = action;
    }

    public Set<String> getCards_id() {
        return cards_id;
    }

    public void setCards_id(Set<String> cards_id) {
        this.cards_id = cards_id;
    }
}
