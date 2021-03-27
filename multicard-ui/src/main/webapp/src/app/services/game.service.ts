import {Injectable, OnDestroy} from '@angular/core';
import {ActionType, DirectionType, StackAction} from '../model/game.model';
import {BehaviorSubject, Observable, Subject, Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {HttpClient} from '@angular/common/http';
import {take} from 'rxjs/operators';
import {
  Action,
  ActionDTO,
  CardDTO,
  GameDTO,
  GameMessage,
  Gamestate,
  GameStateMessage,
  PlayedCardMessage,
  PlayedCardsDTO,
  PlayerDTO,
  PlayersPositionedMessage,
  RevertLastPlayerActionMessage,
  ScoreDTO,
  SetScoreMessage
} from '../../app-gen/generated-model';

const GAME_REST_API_URL = '/api/Games';

@Injectable({providedIn: 'root'})
export class GameService implements OnDestroy {

  private gameId!: string;
  private playerId!: string;
  private stompQueueSubscription!: Subscription;
  // @ts-ignore
  private gameSubject: BehaviorSubject<GameDTO> = new BehaviorSubject<GameDTO>(null);
  private stackSubject: Subject<StackAction> = new Subject();
  private gameStartedByThisClient = false;
  private unsubscribe = new Subject();

  constructor(
    private http: HttpClient,
    private rxStompService: RxStompService) {
  }

  createGame(gameName: string): Observable<GameDTO> {
    return this.http.post<GameDTO>(GAME_REST_API_URL, {}, {params: {gameTitle: gameName}});
  }

  loadGame(gameId: string): Observable<GameDTO> {
    return this.http.get<GameDTO>(`${GAME_REST_API_URL}/${gameId}`);
  }

  addPlayer(gameId: string, isOrganizer: boolean, playerName: string, position: number, password: string): Observable<PlayerDTO> {
    return this.http.put<PlayerDTO>(`${GAME_REST_API_URL}/${gameId}`, {}, {
      headers: {
        name: playerName,
        isOrganizer: JSON.stringify(isOrganizer),
        position: JSON.stringify(position),
        pwd: password
      }
    });
  }

  initGame(gameId: string, playerId: string): Observable<GameDTO> {
    this.gameId = gameId;
    this.playerId = playerId;
    this.stompQueueSubscription = this.subscribeToTopic();
    this.rxStompService.connected$
      .pipe(take(1))
      .subscribe(() => {
        console.log('subscription to stomp queue established');
      });


    return this.gameSubject.asObservable();
  }

  closeGame() {
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
    this.unsubscribe.next();
  }

  registerStackObserver(): Observable<StackAction> {
    return this.stackSubject.asObservable();
  }

  startGame(initiationFromOtherPlayer = false) {
    const game = this.gameSubject.getValue();

    if (!initiationFromOtherPlayer) {
      if (game === null || game.state !== Gamestate.READYTOSTART) {
        console.error('das Game ist nicht im Zustand READYTOSTART und kann deshalb nicht gestartet werden');
        return;
      }

      // if (!this.isUserGameOrganizer(game)) {
      //   console.error('der Spieler ist nicht der Organisator des Spiels und kann das Spiel deshalb nicht starten');
      //   return;
      // }

      this.gameStartedByThisClient = true;
      this.sendWebsocketGameMessage(Action.CLIENT_START_GAME);
    }


    game.state = Gamestate.STARTED;
    this.giveCards(0, 9, 3, game.players.length);
  }

  endRound() {
    this.sendWebsocketGameMessage(Action.CLIENT_SHOW_ALL_PLAYER_STACKS);
  }

  setScore(score: ScoreDTO) {
    this.sendWebsocketSetScoreMessage(score);
  }

  startNewRound() {
    this.sendWebsocketGameMessage(Action.CLIENT_GAME_RESET);
  }

  cardPlayed(card: CardDTO) {
    let game = this.gameSubject.getValue();
    if (!game.playedCards) {
      game.playedCards = {onSameStack: false, cards: []};
    }
    if (!game.playedCards.cards) {
      game.playedCards.cards = [];
    }
    game.playedCards.cards = [...game.playedCards.cards, {...card, playerId: this.playerId}];

    game.players[0].hand.cards = game.players[0].hand.cards.filter(c => c.id !== card.id);
    game = {...game, players: [{...game.players[0]}, ...game.players.slice(1)]};
    this.gameSubject.next(game);

    this.sendWebsocketPlayedCardMessage(card);
  }

  tableCardsTakenByUser(cards: CardDTO[]) {
    let game = this.gameSubject.getValue();
    if (!game.players[0].stacks) {
      game.players[0].stacks = [];
    }
    if (game.players[0].stacks.length === 0) {
      game.players[0].stacks.push({id: '', cards: []});
    }

    cards.forEach(c => c.faceUp = false);

    game.players[0].stacks[0].cards = [...game.players[0].stacks[0].cards, ...cards];
    game.playedCards.cards = [];
    game = {...game, players: [{...game.players[0]}, ...game.players.slice(1)]};
    this.gameSubject.next(game);

    this.sendWebsocketGameMessage(Action.CLIENT_PLAYED_CARDS_TAKEN);
  }

  revertLastAction() {
    this.sendWebsocketRevertLastPlayerActionMessage(this.gameSubject.getValue().lastAction);
  }

  changePlayerPosition(oldIndex: number, newIndex: number) {
    const game = this.gameSubject.getValue();
    game.players.splice(newIndex, 0, game.players.splice(oldIndex, 1)[0]);
    game.players.forEach((p, i) => p.position = i + 1);

    this.sendWebsocketPlayersPositionedMessage(game.players);
  }

  isLastCardPLayedByUser(playedCards: PlayedCardsDTO | undefined) {
    if (playedCards && playedCards.cards) {
      return playedCards.cards[playedCards.cards.length - 1]?.playerId === this.playerId;
    }
    return false;
  }

  haveAllPlayersPlayed(playedCards: PlayedCardsDTO | undefined) {
    return new Set(playedCards?.cards?.map(c => c.playerId)).size >= 4;
  }

  isRevertStackTakenAllowed() {
    const game = this.gameSubject.getValue();
    return game.state === Gamestate.STARTED
      && game.lastAction?.action === Action.CLIENT_PLAYED_CARDS_TAKEN
      && game.lastAction.playerId === game.players[0].id;
  }

  ngOnDestroy(): void {
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
  }

  private subscribeToTopic() {
    return this.rxStompService
      .watch(`/queue/${this.gameId}/${this.playerId}`)
      .subscribe((message: Message) => {
        this.handleWebsocketMessage(JSON.parse(message.body));
      });
  }

  private handleWebsocketMessage(message: GameMessage) {
    console.log('message of type=' + message.command + ' received');
    switch (message.command) {
      case Action.GAME_STATE:
        const gameStateMessage = message as GameStateMessage;
        const sortedPLayers = gameStateMessage.game.players.sort((p1, p2) => p1.position - p2.position);
        const indexCurrUser = sortedPLayers.findIndex(p => p.id === this.playerId);
        // sortiere die Players so, dass der erste PLayer in der Liste dem current User entspricht
        gameStateMessage.game.players = [...gameStateMessage.game.players.slice(indexCurrUser),
          ...gameStateMessage.game.players.slice(0, indexCurrUser)];

        this.gameSubject.next(gameStateMessage.game);
        break;

      case Action.START_GAME:
        if (!this.gameStartedByThisClient) {
          this.startGame(true);
        }
        break;
    }
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
        const game = this.gameSubject.getValue();
        const currPlayer = game.players[i % numberOfPLayers] = {...game.players[i % numberOfPLayers]};
        if (!currPlayer.hand) {
          currPlayer.hand = {id: '', cardCount: 0, cards: []};
        }
        currPlayer.hand.cardCount += numberOfPLayerCardsPerTurn;
        game.players = [...game.players];

        this.giveCards(++i, numberOfTotalCardsPerPlayer, numberOfPLayerCardsPerTurn, numberOfPLayers);
        this.gameSubject.next({...game});
      }, 200);
    } else {
      this.sendWebsocketGameMessage(Action.CLIENT_REQUEST_STATE);
    }
  }

  private isUserGameOrganizer(game: GameDTO) {
    return game?.players[0]?.organizer;
  }

  private sendWebsocketGameMessage(command: Action) {
    const message: GameMessage = {command, messageName: 'GameMessage'};
    this.sendWebsocketMessage(message);
  }

  private sendWebsocketPlayersPositionedMessage(players: PlayerDTO[]) {
    const message: PlayersPositionedMessage = {
      command: Action.CLIENT_PLAYERS_POSITIONED,
      players,
      messageName: 'PlayersPositionedMessage'
    };
    this.sendWebsocketMessage(message);
  }

  private sendWebsocketPlayedCardMessage(card: CardDTO) {
    const message: PlayedCardMessage = {command: Action.CLIENT_CARD_PLAYED, card, messageName: 'PlayedCardMessage'};
    this.sendWebsocketMessage(message);
  }

  private sendWebsocketRevertLastPlayerActionMessage(lastAction: ActionDTO) {
    const message: RevertLastPlayerActionMessage =
      {
        command: Action.CLIENT_REVERT_LAST_PLAYER_ACTION,
        actionId: lastAction.id,
        messageName: 'RevertLastPlayerActionMessage'
      };
    this.sendWebsocketMessage(message);
  }

  private sendWebsocketSetScoreMessage(score: ScoreDTO) {
    const message: SetScoreMessage = {command: Action.CLIENT_SET_SCORE, score, messageName: 'SetScoreMessage'};
    this.sendWebsocketMessage(message);
  }

  private sendWebsocketMessage(message: GameMessage) {
    this.rxStompService.publish({
      destination: `/app/${this.gameId}/${this.playerId}`,
      body: JSON.stringify(message)
    });
  }
}
