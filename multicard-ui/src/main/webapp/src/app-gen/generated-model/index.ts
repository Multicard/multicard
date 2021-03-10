/* tslint:disable */
/* eslint-disable */

export interface CardDTO extends Comparable<CardDTO> {
    id: string;
    name: string;
    sort: number;
    faceUp: boolean;
}

export interface GameDTO {
    id: string;
    title: string;
    state: Gamestate;
    players: PlayerDTO[];
    stacks: StackDTO[];
    playedCards?: PlayedCards;
}

export interface HandDTO {
    id: string;
    cardCount: number;
    cards: CardDTO[];
}

export interface PlayerDTO {
    id: string;
    name: string;
    position: number;
    hand: HandDTO;
    stacks: StackDTO[];
    organizer: boolean;
    playerReady: boolean;
}

export interface StackDTO {
    id: string;
    cards: CardDTO[];
}

export interface GameMessage {
    command: Action;
    game: GameDTO;
}

export interface Comparable<T> {
}

export enum Gamestate {
    INITIAL = "INITIAL",
    READYTOSTART = "READYTOSTART",
    STARTED = "STARTED",
    ENDED = "ENDED",
}

export enum Action {
    CLIENT_GAME_READY = "CLIENT_GAME_READY",
    CLIENT_PLAYER_READY = "CLIENT_PLAYER_READY",
    CLIENT_START_GAME = "CLIENT_START_GAME",
    CLIENT_REQUEST_STATE = "CLIENT_REQUEST_STATE",
    START_GAME = "START_GAME",
    GAME_STATE = "GAME_STATE",
}

export interface PlayedCards {
  onSameStack: boolean;
  idOfStartingPlayer: string;
  cards: CardDTO[];
}

