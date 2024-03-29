/* tslint:disable */
/* eslint-disable */

export interface GameDTO {
    id: string;
    title: string;
    state: Gamestate;
    players: PlayerDTO[];
    stacks: StackDTO[];
    playedCards: PlayedCardsDTO;
    lastAction: ActionDTO;
    currentRound: number;
    scores: ScoreDTO[];
}

export interface GameMessage {
    messageName: "GameMessage" | "GameStateMessage" | "PlayersPositionedMessage" | "PlayedCardMessage" | "RevertLastPlayerActionMessage" | "SetScoreMessage";
    command: Action;
}

export interface GameStateMessage extends GameMessage {
    messageName: "GameStateMessage";
    game: GameDTO;
}

export interface PlayersPositionedMessage extends GameMessage {
    messageName: "PlayersPositionedMessage";
    players: PlayerDTO[];
}

export interface PlayedCardMessage extends GameMessage {
    messageName: "PlayedCardMessage";
    card: CardDTO;
}

export interface RevertLastPlayerActionMessage extends GameMessage {
    messageName: "RevertLastPlayerActionMessage";
    actionId: string;
}

export interface PlayerDTO {
    id: string;
    name: string;
    position: number;
    hand: HandDTO;
    stacks: StackDTO[];
    organizer: boolean;
    alive: boolean;
}

export interface StackDTO {
    id: string;
    cards: CardDTO[];
}

export interface PlayedCardsDTO {
    cards: PlayedCardDTO[];
    onSameStack: boolean;
}

export interface ActionDTO {
    id: string;
    playerId: string;
    action: Action;
}

export interface ScoreDTO {
    id: string;
    round: number;
    playerScores: PlayerScoreDTO[];
}

export interface SetScoreMessage extends GameMessage {
    messageName: "SetScoreMessage";
    score: ScoreDTO;
}

export interface CardDTO extends Comparable<CardDTO> {
    id: string;
    name: string;
    sort: number;
    faceUp: boolean;
}

export interface HandDTO {
    id: string;
    cardCount: number;
    cards: CardDTO[];
}

export interface PlayedCardDTO extends CardDTO {
    playerId: string;
}

export interface PlayerScoreDTO {
    playerId: string;
    score: number;
}

export interface Comparable<T> {
}

export type GameMessageUnion = GameStateMessage | PlayedCardMessage | PlayersPositionedMessage | RevertLastPlayerActionMessage | SetScoreMessage;

export enum Gamestate {
    INITIAL = "INITIAL",
    READYTOSTART = "READYTOSTART",
    STARTED = "STARTED",
    ROUND_ENDED = "ROUND_ENDED",
    GAME_ENDED = "GAME_ENDED",
}

export enum Action {
    CLIENT_PLAYERS_POSITIONED = "CLIENT_PLAYERS_POSITIONED",
    CLIENT_START_ROUND = "CLIENT_START_ROUND",
    CLIENT_REQUEST_STATE = "CLIENT_REQUEST_STATE",
    CLIENT_CARD_PLAYED = "CLIENT_CARD_PLAYED",
    CLIENT_PLAYED_CARDS_TAKEN = "CLIENT_PLAYED_CARDS_TAKEN",
    CLIENT_REVERT_LAST_PLAYER_ACTION = "CLIENT_REVERT_LAST_PLAYER_ACTION",
    CLIENT_END_ROUND = "CLIENT_END_ROUND",
    CLIENT_END_GAME = "CLIENT_END_GAME",
    CLIENT_GAME_RESET = "CLIENT_GAME_RESET",
    CLIENT_SET_SCORE = "CLIENT_SET_SCORE",
    START_GAME = "START_GAME",
    GAME_STATE = "GAME_STATE",
}
