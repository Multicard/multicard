import {Injectable} from '@angular/core';
import {ActionType, DirectionType, Game, GameState, StackAction} from '../model/game.model';
import {Observable, Subject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class GameService {

  private gameState!: Game;

  private stackSubject: Subject<StackAction> = new Subject();

  loadGameState(): Game {
    if (this.gameState === undefined) {
      const gc: Game = {
        id: '123456',
        title: 'fröhliche Schieberrunde',
        state: GameState.readyToStart,
        playerIdOfCurrentUser: 'player1',
        players: [
          {
            id: 'player3',
            name: 'Miriam',
            isOrganizer: false,
            hand: {numberOfCards: 0},
            stacks: []
          },
          {
            id: 'player4',
            name: 'Housi',
            isOrganizer: false,
            hand: {numberOfCards: 0},
            stacks: []
          },
          {
            id: 'player1',
            name: 'Cartman',
            isOrganizer: true,
            hand: {numberOfCards: 0},
            stacks: []
          },
          {
            id: 'player2',
            name: 'Tina',
            isOrganizer: false,
            hand: {numberOfCards: 0},
            stacks: []
          }
        ],
        stacks: [
          {
            id: 'stack1',
            isFaceUp: false,
            numberOfCards: 36
          }
        ]
      };

      this.gameState = gc;
      const indexCurrUser = this.gameState.players.findIndex(p => p.id === gc.playerIdOfCurrentUser);
      // sortiere die Players so, dass der erste PLayer in der Liste dem current User entspricht
      this.gameState.players = [...this.gameState.players.slice(indexCurrUser),
        ...this.gameState.players.slice(0, indexCurrUser)];
    }

    return this.gameState;
  }

  registerStackObserver(): Observable<StackAction> {
    return this.stackSubject.asObservable();
  }

  public startGame() {
    this.gameState.state = GameState.started;
    this.giveCards(0, 9, 3, this.gameState.players.length);
  }

  private giveCards(i: number = 0, numberOfTotalCardsPerPlayer: number, numberOfPLayerCardsPerTurn: number, numberOfPLayers: number) {
    const directions = [DirectionType.left, DirectionType.up, DirectionType.right, DirectionType.down];
    if (i * numberOfPLayerCardsPerTurn < numberOfTotalCardsPerPlayer * numberOfPLayers) {
      this.stackSubject.next({
        action: ActionType.drawCard,
        direction: directions[i % numberOfPLayers],
        numberOfCards: numberOfPLayerCardsPerTurn
      });
      setTimeout(() => {
        this.giveCards(++i, numberOfTotalCardsPerPlayer, numberOfPLayerCardsPerTurn, numberOfPLayers);
        this.gameState.players[i % numberOfPLayers].hand.numberOfCards += numberOfPLayerCardsPerTurn;
      }, 200);
    } else {
      setTimeout(() => this.gameState.players[0].hand.cards = ['AS', '3C', '10H', 'JC', '7D', 'QD', 'KS', 'AH', '5C'], 1000);
    }
  }
}
