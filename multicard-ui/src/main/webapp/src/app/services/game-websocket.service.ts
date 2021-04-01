import {Injectable, OnDestroy} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {BehaviorSubject, Subject, Subscription, timer} from 'rxjs';
import {
  Action,
  ActionDTO,
  CardDTO,
  GameMessage,
  PlayedCardMessage,
  PlayerDTO,
  PlayersPositionedMessage,
  RevertLastPlayerActionMessage,
  ScoreDTO,
  SetScoreMessage
} from '../../app-gen/generated-model';
import {takeUntil} from 'rxjs/operators';
import {Message} from '@stomp/stompjs';
import {RxStompState} from '@stomp/rx-stomp';

@Injectable({
  providedIn: 'root'
})
export class GameWebsocketService implements OnDestroy {

  private gameId!: string;
  private playerId!: string;
  private stompQueueSubscription!: Subscription;
  private unsubscribe = new Subject();
  private stopConnectingTimer = new Subject();
  private connectionActiveSubject = new BehaviorSubject<boolean>(false);

  constructor(
    private rxStompService: RxStompService) {
  }

  ngOnDestroy(): void {
    this.unsubsribeFromQueue();
  }

  get connectionActive$() {
    return this.connectionActiveSubject.asObservable();
  }

  subsribeToQueue(gameId: string, playerId: string, websocketMessageHandler: (message: GameMessage) => void) {
    this.gameId = gameId;
    this.playerId = playerId;

    this.stompQueueSubscription = this.rxStompService
      .watch(`/queue/${gameId}/${playerId}`)
      .subscribe((message: Message) => {
        websocketMessageHandler(JSON.parse(message.body));
      });

    this.rxStompService.connected$
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(() => {
        console.log('subscription to stomp queue established => request game state from backend');
        this.sendWebsocketGameMessage(Action.CLIENT_REQUEST_STATE);
      });

    this.rxStompService.connectionState$
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((state) => {
        console.log('connection state changed to ' + state);
        const connectionActive = state === RxStompState.OPEN;
        if (this.connectionActiveSubject.getValue() !== connectionActive) {
          this.connectionActiveSubject.next(connectionActive);
        }

        // timeout Überwachung und reconnect, falls der Server auf das connecting keine Antwort sendet und stompjs hängt
        // (tritt während dem Starten des Backends auf)
        // https://github.com/stomp-js/stompjs/issues/165
        if (state === RxStompState.CONNECTING) {
          timer(5000)
            .pipe(takeUntil(this.stopConnectingTimer))
            .subscribe(() => {
                console.log('timeout during connecting to stomp queue => try a reconnect');
                this.rxStompService.deactivate().then(() => {
                  console.log('stomp connection disconnected => try to activate it');
                  this.rxStompService.activate();
                });
              }
            );
        } else {
          this.stopConnectingTimer.next();
        }
      });
  }

  unsubsribeFromQueue() {
    this.unsubscribe.next();
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
  }

  sendWebsocketGameMessage(command: Action) {
    const message: GameMessage = {command, messageName: 'GameMessage'};
    this.sendWebsocketMessage(message);
  }

  sendWebsocketPlayersPositionedMessage(players: PlayerDTO[]) {
    const message: PlayersPositionedMessage = {
      command: Action.CLIENT_PLAYERS_POSITIONED,
      players,
      messageName: 'PlayersPositionedMessage'
    };
    this.sendWebsocketMessage(message);
  }

  sendWebsocketPlayedCardMessage(card: CardDTO) {
    const message: PlayedCardMessage = {command: Action.CLIENT_CARD_PLAYED, card, messageName: 'PlayedCardMessage'};
    this.sendWebsocketMessage(message);
  }

  sendWebsocketRevertLastPlayerActionMessage(lastAction: ActionDTO) {
    const message: RevertLastPlayerActionMessage =
      {
        command: Action.CLIENT_REVERT_LAST_PLAYER_ACTION,
        actionId: lastAction.id,
        messageName: 'RevertLastPlayerActionMessage'
      };
    this.sendWebsocketMessage(message);
  }

  sendWebsocketSetScoreMessage(score: ScoreDTO) {
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
