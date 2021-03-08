package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class StackDTO {
    private String id;
    private List<CardDTO> cards = new ArrayList<>();

    public StackDTO(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }
}
