package ch.cas.html5.multicardgame.enums;

public enum Action {
    // Messages vom UI ans Backend
    CLIENT_GAME_READY,
    CLIENT_PLAYER_READY,
    CLIENT_START_GAME,
    CLIENT_REQUEST_STATE,
    CLIENT_CARD_PLAYED,
    CLIENT_PLAYED_CARDS_TAKEN,
    CLIENT_REVERT_LAST_PLAYER_ACTION,
    CLIENT_REVERT_ACTION,

    // Messages vom Backend ans UI
    START_GAME,
    GAME_STATE
}
