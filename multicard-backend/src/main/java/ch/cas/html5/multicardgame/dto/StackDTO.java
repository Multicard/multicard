package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class StackDTO {
    private String id;
    private String topcard;
    private List<CardDTO> cards = new ArrayList<>();

    public StackDTO(String id, String topcard){
        this.id = id;
        this.topcard = topcard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopcard() {
        return topcard;
    }

    public void setTopcard(String topcard) {
        this.topcard = topcard;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }
}
