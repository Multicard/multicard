export interface Game {
  id: string;
  title: string;
  playerIdOfCurrentUser: string;
  players: Player[];
  stacks: Stack[];
}

export interface Player {
  id: string;
  name: string;
  isOrganizer: boolean;
  hand: Hand;
  stacks: Stack[];
}

export interface Stack {
  id: string;
  isFaceUp: boolean;
  numberOfCards: number;
  topCard?: string;
}

export interface Hand {
  cards?: string[];
  numberOfCards: number;
}

export class Card {
  isFaceUp = false;
  card?: string;
}

export interface StackAction {
  action: ActionType;
  direction: DirectionType;
  numberOfCards: number;
}

export enum ActionType {
  drawCard = 'drawCard',
  playCard = 'playCard'
}

export enum DirectionType {
  left = 'left',
  up = 'up',
  right = 'right',
  down = 'down'
}
