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
