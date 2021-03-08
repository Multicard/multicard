package ch.cas.html5.multicardgame.dto;

public class CardDTO {
    private String id;
    private String name;
    private Boolean isFaceUp = false;
    private int sort;

    public CardDTO(String id, String name, int sort){
        this.id = id;
        this.name = name;
        this.sort = sort;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Boolean getFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(Boolean faceUp) {
        isFaceUp = faceUp;
    }
}
