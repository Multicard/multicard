package ch.cas.html5.multicardgame.entity;

public enum Gamestate {
    INITIAL,
    READYTOSTART,
    STARTED,
    ENDED;

//    INITIAL("initial"),READYTOSTART("readyToStart"),STARTED("started"), ENDED("ended");
//
//    private String text;
//
//    Gamestate(String text){this.text = text;}
//
//    public String getText(){return this.text;}
//
//    public static Gamestate fromText(String text){
//        for(Gamestate r : Gamestate.values()){
//            if(r.getText().equals(text)){
//                return r;
//            }
//        }
//        throw new IllegalArgumentException();
//    }

}
