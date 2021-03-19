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

export class Player {
  playerName: string;
  registeredGames: GamePlayer[] = [];

  constructor(playerName: string) {
    this.playerName = playerName;
  }

  static deserializePlayer(serializedPLayer: string | null): Player {
    return serializedPLayer ? JSON.parse(serializedPLayer) : new Player('');
  };
}

export class GamePlayer {
  gameId: string;
  playerId: string;

  constructor(gameId: string, playerId: string) {
    this.gameId = gameId;
    this.playerId = playerId;
  }
}
