/* tslint:disable */
/* eslint-disable */

export interface CardDTO {
    id: string;
    name: string;
    sort: number;
}

export interface GameDTO {
    id: string;
    title: string;
    players: PlayerDTO[];
    stacks: StackDTO[];
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
}

export interface StackDTO {
    id: string;
    topcard: string;
    cards: CardDTO[];
}
