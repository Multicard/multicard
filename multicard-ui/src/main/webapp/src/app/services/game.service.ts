import {Injectable} from '@angular/core';
import {ActionType, DirectionType, StackAction} from '../model/game.model';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {
  Action,
  CardDTO,
  GameDTO,
  GameMessage,
  Gamestate,
  GameStateMessage,
  PlayedCardsDTO,
  PlayerDTO,
  ScoreDTO
} from '../../app-gen/generated-model';
import {GameWebsocketService} from './game-websocket.service';

const GAME_REST_API_URL = '/api/Games';

@Injectable({providedIn: 'root'})
export class GameService {

  private playerId!: string;
  // @ts-ignore
  private gameSubject: BehaviorSubject<GameDTO> = new BehaviorSubject<GameDTO>(null);
  private stackSubject: Subject<StackAction> = new Subject();
  private gameStartedByThisClient = false;
  /*
   * Umgehungslösung, da bei ChangeDetection = OnPush beim drag and drop über Komponentengrenzen die Notifikation im cdkDropList
   * nicht mehr korrekt funktionierte.
   * Die Klasse cdk-drop-list-receiving wurde in der Zielkomponente nicht korrekt gesetzt. Deshalb wird die Zielkomponente
   * jetzt mittels Observable auf diese Subjects über ein ongoing drag and drop notifziert.
   */
  private playerCardDragAndDropOntoTableInProgress: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private tableStackDragAndDropInProgress: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient,
    private gameWebsocketService: GameWebsocketService) {
  }

  createGame(gameName: string): Observable<GameDTO> {
    return this.http.post<GameDTO>(GAME_REST_API_URL, {}, {params: {gameTitle: gameName}});
  }

  loadGame(gameId: string): Observable<GameDTO> {
    return this.http.get<GameDTO>(`${GAME_REST_API_URL}/${gameId}`);
  }

  addPlayer(gameId: string, isOrganizer: boolean, playerName: string, password: string): Observable<PlayerDTO> {
    return this.http.put<PlayerDTO>(`${GAME_REST_API_URL}/${gameId}`, {}, {
      headers: {
        name: playerName,
        isOrganizer: JSON.stringify(isOrganizer),
        pwd: password
      }
    });
  }

  initGame(gameId: string, playerId: string): Observable<GameDTO> {
    this.playerId = playerId;
    this.gameWebsocketService.subsribeToQueue(gameId, playerId,
      (message) => this.handleWebsocketMessage(message));

    return this.gameSubject.asObservable();
  }

  get connectionActive$() {
    return this.gameWebsocketService.connectionActive$;
  }

  closeGame() {
    this.gameWebsocketService.unsubsribeFromQueue();
  }

  getStackObservable() {
    return this.stackSubject.asObservable();
  }

  getPlayerCardDragAndDropOntoTableInProgressObservable() {
    return this.playerCardDragAndDropOntoTableInProgress.asObservable();
  }

  setPlayerCardDragAndDropOntoTableInProgress(inProgress: boolean) {
    this.playerCardDragAndDropOntoTableInProgress.next(inProgress);
  }

  getTableStackDragAndDropInProgressObservable() {
    return this.tableStackDragAndDropInProgress.asObservable();
  }

  setTableStackDragAndDropInProgress(inProgress: boolean) {
    this.tableStackDragAndDropInProgress.next(inProgress);
  }

  startRound(initiationFromOtherPlayer = false) {
    const game = this.gameSubject.getValue();

    if (!initiationFromOtherPlayer) {
      if (game === null || game.state !== Gamestate.READYTOSTART) {
        console.error('das Game ist nicht im Zustand READYTOSTART und kann deshalb nicht gestartet werden');
        return;
      }

      this.gameStartedByThisClient = true;
      this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_START_ROUND);
    }


    game.state = Gamestate.STARTED;
    this.giveCards(0, 9, 3, game.players.length);
  }

  endRound() {
    this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_END_ROUND);
  }

  endGame() {
    this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_END_GAME);
    this.gameSubject.next({...this.gameSubject.getValue(), state: Gamestate.GAME_ENDED});
  }

  setScore(score: ScoreDTO) {
    this.gameWebsocketService.sendWebsocketSetScoreMessage(score);
  }

  startNewRound() {
    this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_GAME_RESET);
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

    this.gameWebsocketService.sendWebsocketPlayedCardMessage(card);
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

    this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_PLAYED_CARDS_TAKEN);
  }

  revertLastAction() {
    this.gameWebsocketService.sendWebsocketRevertLastPlayerActionMessage(this.gameSubject.getValue().lastAction);
  }

  changePlayerPosition(oldIndex: number, newIndex: number) {
    const game = this.gameSubject.getValue();
    game.players.splice(newIndex, 0, game.players.splice(oldIndex, 1)[0]);
    game.players.forEach((p, i) => p.position = i + 1);

    this.gameWebsocketService.sendWebsocketPlayersPositionedMessage(game.players);
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

  private handleWebsocketMessage(message: GameMessage) {
    console.log('message of type=' + message.command + ' received');
    switch (message.command) {
      case Action.GAME_STATE:
        const gameStateMessage = message as GameStateMessage;
        // sortiere die Players so, dass der erste PLayer in der Liste dem current User entspricht
        const sortedPLayers = gameStateMessage.game.players.sort((p1, p2) => p1.position - p2.position);
        const indexCurrUser = sortedPLayers.findIndex(p => p.id === this.playerId);
        gameStateMessage.game.players = [...gameStateMessage.game.players.slice(indexCurrUser),
          ...gameStateMessage.game.players.slice(0, indexCurrUser)];

        this.gameSubject.next(gameStateMessage.game);
        break;

      case Action.START_GAME:
        if (!this.gameStartedByThisClient) {
          this.startRound(true);
        }
        break;
    }
  }

  private giveCards(i: number = 0, numberOfTotalCardsPerPlayer: number, numberOfPLayerCardsPerTurn: number, numberOfPLayers: number) {
    const directions = [DirectionType.left, DirectionType.up, DirectionType.right, DirectionType.down];
    if (i * numberOfPLayerCardsPerTurn < numberOfTotalCardsPerPlayer * numberOfPLayers) {
      // Notifziere den Stappel, wieviele Karten abgehoben werden und in welcher Richtung zum Spieler fliegen
      this.stackSubject.next({
        action: ActionType.drawCard,
        direction: directions[i % numberOfPLayers],
        numberOfCards: numberOfPLayerCardsPerTurn
      });
      // Die Karten in der Hand eines Spielers werden verzögert inkrementiert, damit die Karten vorher
      // animiert vom Stappel Richtung Spieler fliegen können
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
      // erst nachdem alle Karten im UI verteilt sind, wird ein neuer Game Status vom Backend angefordert
      // und die Karten angezeigt, welche der Spieler erhalten hat
      this.gameWebsocketService.sendWebsocketGameMessage(Action.CLIENT_REQUEST_STATE);
    }
  }
}
