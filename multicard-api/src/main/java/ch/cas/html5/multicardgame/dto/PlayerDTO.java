package ch.cas.html5.multicardgame.dto;

import java.util.ArrayList;
import java.util.List;

public class PlayerDTO {
    private String id;
    private String name;
    private Boolean isOrganizer;
    private Boolean isAlive;
    private int position;
    private HandDTO hand;
    private List<StackDTO> stacks = new ArrayList<>();

    public PlayerDTO(String id, String name, Boolean isOrganizer, int position, Boolean isOnline){
        this.id = id;
        this.name = name;
        this.isOrganizer = isOrganizer;
        this.position = position;
        this.isAlive = isOnline;
    }

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

    public Boolean getOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(Boolean organizer) {
        isOrganizer = organizer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public HandDTO getHand() {
        return hand;
    }

    public void setHand(HandDTO hand) {
        this.hand = hand;
    }

    public List<StackDTO> getStacks() {
        return stacks;
    }

    public void setStacks(List<StackDTO> stacks) {
        this.stacks = stacks;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }
}
