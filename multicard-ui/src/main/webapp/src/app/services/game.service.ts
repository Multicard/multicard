import {Injectable, OnDestroy} from '@angular/core';
import {ActionType, DirectionType, Game, GameState, StackAction} from '../model/game.model';
import {Observable, Subject, Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';

@Injectable({providedIn: 'root'})
export class GameService implements OnDestroy {

  private stompQueueSubscription!: Subscription;
  private gameState!: Game;
  private stackSubject: Subject<StackAction> = new Subject();

  constructor(private rxStompService: RxStompService) {
  }

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

      this.subscribeToTopic();
    }

    return this.gameState;
  }

  registerStackObserver(): Observable<StackAction> {
    return this.stackSubject.asObservable();
  }

  startGame() {
    this.gameState.state = GameState.started;
    this.giveCards(0, 9, 3, this.gameState.players.length);
  }

  ngOnDestroy(): void {
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
  }

  private subscribeToTopic() {
    // TODO: Spiel ID und Player ID setzen
    const gameId = '933F4FB5-5144-4932-AF0A-C2098E24D184';
    const playerId = '103F5A8A-7A5B-49F9-89E2-81D58183ED2E';
    this.stompQueueSubscription = this.rxStompService
      .watch(`/queue/$gameId/$playerId`)
      .subscribe((message: any) => {
        console.log(message);
      });
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
      setTimeout(() => {
        this.gameState.players[0].hand.cards = ['AS', '3C', '10H', 'JC', '7D', 'QD', 'KS', 'AH', '5C'];
        this.gameState.playedCards = {
          idOfStartingPlayer: 'player4',
          onSameStack: true,
          cards: [{isFaceUp: true, card: '10H'}, {isFaceUp: true, card: 'AC'}]
        };
      }, 1000);
    }
  }
}
