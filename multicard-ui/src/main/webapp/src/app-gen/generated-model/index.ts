/* tslint:disable */
/* eslint-disable */

export interface CardDTO {
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
    playerReady: boolean;
    organizer: boolean;
}

export interface StackDTO {
    id: string;
    cards: CardDTO[];
}

export interface GameAction {
    command: Action;
    text: string;
}

export enum Gamestate {
    INITIAL = "INITIAL",
    READYTOSTART = "READYTOSTART",
    STARTED = "STARTED",
    ENDED = "ENDED",
}

export enum Action {
    START_GAME = "START_GAME",
}

export interface PlayedCards {
  onSameStack: boolean;
  idOfStartingPlayer: string;
  cards: CardDTO[];
}

