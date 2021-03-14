package ch.cas.html5.multicardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="card")
public class Card {

    @Id
    @Column(unique = true, name = "id", nullable = false)
    private String id = UUID.randomUUID().toString().toUpperCase();

    @Column(name="card_name")
    private String name;

    @Column(name="sort")
    private int sort;

    @JsonIgnoreProperties("cards")
    @ManyToOne(optional = true)
    private Stack stack;

    @JsonIgnoreProperties("cards")
    @ManyToOne(optional = true)
    private Hand hand;

    @JsonIgnoreProperties("cards")
    @ManyToOne(optional = true)
    private Player player;

    @JsonIgnoreProperties("playedcards")
    @ManyToOne(optional = true)
    private PlayedCards playedcards;

//    @OneToOne(mappedBy = "topCard")
//    private Stack stackTopCard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stack getStack() {
        return stack;
    }

    public void setStack(Stack stack) {
        this.stack = stack;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getSort() { return sort; }

    public void setSort(int sort) { this.sort = sort; }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayedCards getPlayedcards() {
        return playedcards;
    }

    public void setPlayedcards(PlayedCards playedcards) {
        this.playedcards = playedcards;
    }

    //    public Stack getStackTopCard() { return stackTopCard; }
//
//    public void setStackTopCard(Stack stackTopCard) {
//        this.stackTopCard = stackTopCard;
//    }
}
