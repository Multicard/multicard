package ch.cas.html5.multicardgame.dto;

public class CardDTO implements Comparable<CardDTO> {
    private String id;
    private String name;
    private Boolean isFaceUp = false;
    private int sort;

    public CardDTO() {
    }

    public CardDTO(String id, String name, int sort, Boolean isFaceUp){
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.setFaceUp(isFaceUp);
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

    @Override
    public int compareTo(CardDTO o) {
        return (this.getSort() < o.getSort() ? -1 :
                (this.getSort() == o.getSort() ? 0 : 1));
    }
}
