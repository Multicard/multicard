import {Injectable, OnDestroy} from '@angular/core';
import {ActionType, DirectionType, Game, StackAction} from '../model/game.model';
import {BehaviorSubject, Observable, Subject, Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {HttpClient} from '@angular/common/http';
import {take} from 'rxjs/operators';
import {GameDTO, Gamestate} from '../../app-gen/generated-model';

const restApiUrl = '/api/Games';

// TODO: Spiel ID und Player ID dynamisch lesen
const gameId = 'EA9CA14C-AA81-4A62-8536-E68099975130';
const playerId = '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD';

@Injectable({providedIn: 'root'})
export class GameService implements OnDestroy {

  private stompQueueSubscription!: Subscription;
  // @ts-ignore
  private gameSubject: BehaviorSubject<GameDTO> = new BehaviorSubject<Game>(null);
  private stackSubject: Subject<StackAction> = new Subject();

  constructor(private http: HttpClient, private rxStompService: RxStompService) {
  }

  loadGameState(): Observable<GameDTO> {
    if (this.gameSubject.getValue() === null) {
      this.subscribeToTopic();
      this.rxStompService.connected$
        .pipe(take(1))
        .subscribe(() => {
          this.initGame();
        });
    }

    return this.gameSubject.asObservable();
  }

  registerStackObserver(): Observable<StackAction> {
    return this.stackSubject.asObservable();
  }

  startGame() {
    this.rxStompService.publish({
      destination: `/app/${gameId}/${playerId}`,
      body: JSON.stringify({command: 'START_GAME', text: 'test'})
    });

    this.gameSubject.getValue().state = Gamestate.STARTED;
    this.giveCards(0, 9, 3, this.gameSubject.getValue().players.length);
  }

  ngOnDestroy(): void {
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
  }

  private initGame() {
    this.http.put<string>(restApiUrl + '/EA9CA14C-AA81-4A62-8536-E68099975130', '').subscribe();

    const gameState: GameDTO = {
      id: '123456',
      title: 'fröhliche Schieberrunde',
      state: Gamestate.READYTOSTART,
      players: [
        {
          id: 'player3',
          name: 'Miriam',
          position: 0,
          playerReady: true,
          organizer: false,
          hand: {id: '1', cardCount: 0, cards: []},
          stacks: []
        },
        {
          id: 'player4',
          name: 'Housi',
          position: 0,
          playerReady: true,
          organizer: false,
          hand: {id: '2', cardCount: 0, cards: []},
          stacks: []
        },
        {
          id: '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD',
          name: 'Cartman',
          position: 0,
          playerReady: true,
          organizer: true,
          hand: {id: '3', cardCount: 0, cards: []},
          stacks: []
        },
        {
          id: 'player2',
          name: 'Tina',
          position: 0,
          playerReady: true,
          organizer: false,
          hand: {id: '4', cardCount: 0, cards: []},
          stacks: []
        }
      ],
      stacks: [
        {
          id: 'stack1',
          cards: [
            {id: '1', faceUp: false, name: 'N/A', sort: 1},
            {id: '2', faceUp: false, name: 'N/A', sort: 2},
            {id: '3', faceUp: false, name: 'N/A', sort: 3},
            {id: '4', faceUp: false, name: 'N/A', sort: 4},
            {id: '5', faceUp: false, name: 'N/A', sort: 5},
            {id: '6', faceUp: false, name: 'N/A', sort: 6},
            {id: '7', faceUp: false, name: 'N/A', sort: 7},
            {id: '8', faceUp: false, name: 'N/A', sort: 8},
            {id: '9', faceUp: false, name: 'N/A', sort: 9},
            {id: '10', faceUp: false, name: 'N/A', sort: 10},
            {id: '11', faceUp: false, name: 'N/A', sort: 11},
            {id: '12', faceUp: false, name: 'N/A', sort: 12},
            {id: '13', faceUp: false, name: 'N/A', sort: 13},
            {id: '14', faceUp: false, name: 'N/A', sort: 14},
            {id: '15', faceUp: false, name: 'N/A', sort: 15},
            {id: '16', faceUp: false, name: 'N/A', sort: 16},
            {id: '17', faceUp: false, name: 'N/A', sort: 17},
            {id: '18', faceUp: false, name: 'N/A', sort: 18},
            {id: '19', faceUp: false, name: 'N/A', sort: 19},
            {id: '20', faceUp: false, name: 'N/A', sort: 20},
            {id: '21', faceUp: false, name: 'N/A', sort: 21},
            {id: '22', faceUp: false, name: 'N/A', sort: 22},
            {id: '23', faceUp: false, name: 'N/A', sort: 23},
            {id: '24', faceUp: false, name: 'N/A', sort: 24},
            {id: '25', faceUp: false, name: 'N/A', sort: 25},
            {id: '26', faceUp: false, name: 'N/A', sort: 26},
            {id: '27', faceUp: false, name: 'N/A', sort: 27},
            {id: '28', faceUp: false, name: 'N/A', sort: 28},
            {id: '29', faceUp: false, name: 'N/A', sort: 29},
            {id: '30', faceUp: false, name: 'N/A', sort: 30},
            {id: '31', faceUp: false, name: 'N/A', sort: 31},
            {id: '32', faceUp: false, name: 'N/A', sort: 32},
            {id: '33', faceUp: false, name: 'N/A', sort: 33},
            {id: '34', faceUp: false, name: 'N/A', sort: 34},
            {id: '35', faceUp: false, name: 'N/A', sort: 35},
            {id: '36', faceUp: false, name: 'N/A', sort: 36},
          ]
        }
      ]
    };

    const sortedPLayers = gameState.players.sort((p1, p2) => p1.position - p2.position);
    const indexCurrUser = sortedPLayers.findIndex(p => p.id === playerId);
    // sortiere die Players so, dass der erste PLayer in der Liste dem current User entspricht
    gameState.players = [...gameState.players.slice(indexCurrUser),
      ...gameState.players.slice(0, indexCurrUser)];

    this.gameSubject.next(gameState);
  }

  private subscribeToTopic() {
    this.stompQueueSubscription = this.rxStompService
      .watch(`/queue/${gameId}/${playerId}`)
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
        this.gameSubject.getValue().players[i % numberOfPLayers] = {...this.gameSubject.getValue().players[i % numberOfPLayers]};
        this.gameSubject.getValue().players[i % numberOfPLayers].hand.cardCount += numberOfPLayerCardsPerTurn;
      }, 200);
    } else {
      setTimeout(() => {
        const gameState = this.gameSubject.getValue();
        gameState.players[0].hand.cards = [
          {id: '1', faceUp: true, name: 'AS', sort: 7},
          {id: '2', faceUp: true, name: '3C', sort: 1},
          {id: '3', faceUp: true, name: '10H', sort: 4},
          {id: '4', faceUp: true, name: 'JC', sort: 3},
          {id: '5', faceUp: true, name: '7D', sort: 8},
          {id: '6', faceUp: true, name: 'QD', sort: 9},
          {id: '7', faceUp: true, name: 'KS', sort: 6},
          {id: '8', faceUp: true, name: 'AH', sort: 5},
          {id: '9', faceUp: true, name: '5C', sort: 2}];
        gameState.players[0].stacks = [{
          id: '1232', cards: [
            {id: '1', faceUp: false, name: '', sort: 1},
            {id: '2', faceUp: false, name: '', sort: 2},
            {id: '3', faceUp: false, name: '', sort: 3},
            {id: '4', faceUp: false, name: '', sort: 4},
            {id: '5', faceUp: false, name: '', sort: 5},
            {id: '6', faceUp: false, name: '', sort: 6},
            {id: '7', faceUp: false, name: '', sort: 7},
            {id: '8', faceUp: false, name: '', sort: 8}
          ]
        }];
        gameState.players[2].stacks = [{
          id: '1275', cards: [
            {id: '1', faceUp: false, name: '', sort: 1},
            {id: '2', faceUp: false, name: '', sort: 2},
            {id: '3', faceUp: false, name: '', sort: 3},
            {id: '4', faceUp: false, name: '', sort: 4}
          ]
        }];
        gameState.playedCards = {
          idOfStartingPlayer: 'player4',
          onSameStack: false,
          cards: [{faceUp: true, name: '10H', id: '1', sort: 1}, {faceUp: true, name: 'AC', id: '2', sort: 1},
            {faceUp: true, name: '6D', id: '3', sort: 1}, {faceUp: true, name: '7S', id: '4', sort: 1},
            {faceUp: true, name: 'JH', id: '5', sort: 1}, {faceUp: true, name: 'QD', id: '6', sort: 1}]
        };
        gameState.players = [{...gameState.players[0]}, gameState.players[1], {...gameState.players[2]}, gameState.players[3]];
        this.gameSubject.next({...gameState});
      }, 1000);
    }
  }
}
