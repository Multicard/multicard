package ch.cas.html5.multicardgame.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="playedcard")
public class PlayedCard extends Card{

    @JsonIgnoreProperties("playedcard")
    @ManyToOne(optional = true)
    private Player player;

    @JsonIgnoreProperties("playedcard")
    @ManyToOne(optional = true)
    private PlayedCards playedcards;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayedCards getPlayedcards() {
        return playedcards;
    }

    public void setPlayedcards(PlayedCards playedCards) {
        this.playedcards = playedCards;
    }

}
