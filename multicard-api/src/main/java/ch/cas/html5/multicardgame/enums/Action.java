package ch.cas.html5.multicardgame.enums;

public enum Action {
    // Messages vom UI ans Backend
    CLIENT_PLAYERS_POSITIONED,
    CLIENT_START_ROUND,
    CLIENT_REQUEST_STATE,
    CLIENT_CARD_PLAYED,
    CLIENT_PLAYED_CARDS_TAKEN,
    CLIENT_REVERT_LAST_PLAYER_ACTION,
    CLIENT_REVERT_ACTION,
    CLIENT_IS_ALIVE,
    CLIENT_END_ROUND,
    CLIENT_END_GAME,
    CLIENT_GAME_RESET,
    CLIENT_SET_SCORE,

    // Messages vom Backend ans UI
    START_GAME,
    GAME_STATE
}
