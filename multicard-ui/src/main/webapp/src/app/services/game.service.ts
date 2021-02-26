import {Injectable} from '@angular/core';
import {GameConfiguration, GameState} from '../model/game.model';

@Injectable({providedIn: 'root'})
export class GameService {

  private gameConfiguration!: GameConfiguration;
  private gameState!: GameState;

  getGameConfiguration(): GameConfiguration {
    if (this.gameConfiguration === undefined) {
      const gc: GameConfiguration = {
        id: '123456',
        title: 'fröhliche Schieberrunde',
        playerIdOfCurrentUser: 'player1',
        players: [
          {
            id: 'player3',
            name: 'Miriam',
            isOrganizer: false
          },
          {
            id: 'player4',
            name: 'Housi',
            isOrganizer: false
          },
          {
            id: 'player1',
            name: 'Cartman',
            isOrganizer: true
          },
          {
            id: 'player2',
            name: 'Tina',
            isOrganizer: false
          }
        ]
      };

      this.gameConfiguration = gc;
      const indexCurrUser = this.gameConfiguration.players.findIndex(p => p.id === gc.playerIdOfCurrentUser);
      // sortiere die Players so, dass der erste PLayer in der Liste dem current User entspricht
      this.gameConfiguration.players = [...this.gameConfiguration.players.slice(indexCurrUser),
        ...this.gameConfiguration.players.slice(0, indexCurrUser)];
    }

    return this.gameConfiguration;
  }

  getGameState(): GameState {
    if (this.gameState === undefined) {
      this.gameState = {
        players: [{
          id: 'player3',
          hand: {numberOfCards: 0},
          stacks: []
        }, {
          id: 'player4',
          hand: {numberOfCards: 0},
          stacks: []
        }, {
          id: 'player1',
          hand: {numberOfCards: 0},
          stacks: []
        }, {
          id: 'player2',
          hand: {numberOfCards: 0},
          stacks: []
        }],
        stacks: [
        {
          id: 'stack',
          isFaceUp: false,
          numberOfCards: 36
        }
      ]};
    }

    return this.gameState;
  }
}
