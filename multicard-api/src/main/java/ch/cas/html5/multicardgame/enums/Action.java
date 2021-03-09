package ch.cas.html5.multicardgame.enums;

public enum Action {
    // Messages vom UI ans Backend
    CLIENT_GAME_READY,
    CLIENT_PLAYER_READY,
    CLIENT_START_GAME,
    CLIENT_REQUEST_STATE,

    // Messages vom Backend ans UI
    START_GAME,
    GAME_STATE
}
