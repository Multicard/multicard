export interface GameConfiguration {
  id: string;
  title: string;
  playerIdOfCurrentUser: string;
  players: PlayerConfiguration[];
}

export interface PlayerConfiguration {
  id: string;
  name: string;
  isOrganizer: boolean;
}

export interface GameState {
  players: Player[];
  stacks: Stack[];
}

export interface Stack {
  id: string;
  isFaceUp: boolean;
  numberOfCards: number;
  topCard?: string;
}

export interface Player {
  id: string;
  hand: Hand;
  stacks: Stack[];
}

export interface Hand {
  cards?: string[];
  numberOfCards: number;
}

export class Card {
  isFaceUp = false;
  card?: string;
  back = 'BLUE_BACK.svg';
}
