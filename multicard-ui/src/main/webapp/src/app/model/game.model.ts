export interface Game {
  id: string;
  title: string;
  state: GameState;
  playerIdOfCurrentUser: string;
  players: Player[];
  stacks: Stack[];
  playedCards?: PlayedCards;
}

export enum GameState {
  initial = 'initial',
  readyToStart = 'readyToStart',
  started = 'started',
  ended = 'ended'
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

export interface PlayedCards {
  onSameStack: boolean;
  idOfStartingPlayer: string;
  cards: Card[];
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
