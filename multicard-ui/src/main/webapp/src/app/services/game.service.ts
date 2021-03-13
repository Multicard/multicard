import {Injectable, OnDestroy} from '@angular/core';
import {ActionType, DirectionType, StackAction} from '../model/game.model';
import {BehaviorSubject, Observable, Subject, Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {HttpClient} from '@angular/common/http';
import {take} from 'rxjs/operators';
import {
  Action,
  CardDTO,
  GameDTO,
  GameMessage,
  Gamestate,
  GameStateMessage,
  PlayedCardMessage,
  PlayedCardsDTO,
  PlayerDTO,
  PlayersPositionedMessage
} from '../../app-gen/generated-model';

const restApiUrl = '/api/Games';

@Injectable({providedIn: 'root'})
export class GameService implements OnDestroy {

  private gameId!: string;
  private playerId!: string;
  private stompQueueSubscription!: Subscription;
  // @ts-ignore
  private gameSubject: BehaviorSubject<GameDTO> = new BehaviorSubject<GameDTO>(null);
  private stackSubject: Subject<StackAction> = new Subject();
  private gameStartedByThisClient = false;

  constructor(private http: HttpClient, private rxStompService: RxStompService) {
  }

  initGame(gameId: string, playerId: string): Observable<GameDTO> {
    this.gameId = gameId;
    this.playerId = playerId;
    this.subscribeToTopic();
    this.rxStompService.connected$
      .pipe(take(1))
      .subscribe(() => {
        // TODO Senden von Game Ready zu der Konfiguration des Games verschieben
        if (playerId === '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD') {
          this.sendWebsocketGameMessage(Action.CLIENT_GAME_READY);
        }
        // this.http.put<string>(restApiUrl + '/EA9CA14C-AA81-4A62-8536-E68099975130', '').subscribe(() =>
        //   this.sendWebsocketMessage(Action.CLIENT_PLAYER_READY)
        // );
        this.sendWebsocketGameMessage(Action.CLIENT_PLAYER_READY);
      });

    // TODO remove Mockcall
    //this.initMockInitialWebsocketMessage();
    return this.gameSubject.asObservable();
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

      if (!this.isUserGameOrganizer(game)) {
        console.error('der Spieler ist nicht der Organisator des Spiels und kann das Spiel deshalb nicht starten');
        return;
      }

      game.state = Gamestate.STARTED;
      this.gameStartedByThisClient = true;
      this.sendWebsocketGameMessage(Action.CLIENT_START_GAME);

      // TODO remove Mockcall
      //this.initMockWebsocketGameStartMessages();
    }


    this.giveCards(0, 9, 3, game.players.length);
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

    // TODO remove Mockcall
    this.initMockWebsocketGameCardAddedMessage(card);
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

  ngOnDestroy(): void {
    if (this.stompQueueSubscription !== undefined) {
      this.stompQueueSubscription.unsubscribe();
    }
  }

  private subscribeToTopic() {
    this.stompQueueSubscription = this.rxStompService
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

        this.giveCards(++i, numberOfTotalCardsPerPlayer, numberOfPLayerCardsPerTurn, numberOfPLayers);
      }, 200);
    } else {
      this.sendWebsocketGameMessage(Action.CLIENT_REQUEST_STATE);

      // TODO remove Mockcall
      //this.initMockWebsocketGameStartedMessages();
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

  private sendWebsocketMessage(message: GameMessage) {
    this.rxStompService.publish({
      destination: `/app/${this.gameId}/${this.playerId}`,
      body: JSON.stringify(message)
    });
  }

  private initMockInitialWebsocketMessage() {
    setTimeout(
      () => this.handleWebsocketMessage({
        command: Action.GAME_STATE, game: {
          id: 'EA9CA14C-AA81-4A62-8536-E68099975130',
          title: 'Jassrunde 0815',
          state: Gamestate.READYTOSTART,
          players: [
            {
              id: '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD',
              name: 'Chefspieler',
              position: 1,
              hand: null,
              stacks: [],
              organizer: true,
              playerReady: true
            },
            {
              id: '53BE4441-C575-41B9-BECD-9B2A634C771B',
              name: 'Spieler2',
              position: 2,
              hand: null,
              stacks: [],
              organizer: false,
              playerReady: false
            },
            {
              id: '0EB0DD34-8DD9-44E6-8D21-9265E190500A',
              name: 'Spieler3',
              position: 3,
              hand: null,
              stacks: [],
              organizer: false,
              playerReady: false
            },
            {
              id: '8EE3E68B-C9B8-41B2-BEC8-6BBE4916B817',
              name: 'Spieler4',
              position: 4,
              hand: null,
              stacks: [],
              organizer: false,
              playerReady: false
            }
          ],
          stacks: [
            {
              id: 'AFC93B20-8359-44E9-866F-F887BDDB3B41',
              cards: [
                {
                  id: '310BB9D2-23AC-4A7E-B259-CEC99FF70951',
                  name: 'N/A',
                  sort: 26,
                  faceUp: false
                },
                {
                  id: '73161D6A-C8E8-4D76-AB8A-CFA8F43A26D0',
                  name: 'N/A',
                  sort: 29,
                  faceUp: false
                },
                {
                  id: '86D4E327-3646-4E93-80D3-FD63A260F39F',
                  name: 'N/A',
                  sort: 14,
                  faceUp: false
                },
                {
                  id: 'FDD04516-2057-4293-8207-0F746F175DCB',
                  name: 'N/A',
                  sort: 22,
                  faceUp: false
                },
                {
                  id: '70860425-4A94-4305-9D06-0E32FF592178',
                  name: 'N/A',
                  sort: 7,
                  faceUp: false
                },
                {
                  id: '6191E1CF-D992-4E3F-9584-54638FDEA88D',
                  name: 'N/A',
                  sort: 24,
                  faceUp: false
                },
                {
                  id: '1D47B2FF-755F-4F9F-9868-7FE0B1F90309',
                  name: 'N/A',
                  sort: 3,
                  faceUp: false
                },
                {
                  id: '3B19729F-22CF-4460-904C-3BDCBFBC6767',
                  name: 'N/A',
                  sort: 27,
                  faceUp: false
                },
                {
                  id: 'E87B60EF-85F7-4451-9658-155F35054BC0',
                  name: 'N/A',
                  sort: 15,
                  faceUp: false
                },
                {
                  id: '30A7F30B-5221-4EA6-BCEB-7F0F828BC931',
                  name: 'N/A',
                  sort: 6,
                  faceUp: false
                },
                {
                  id: 'AE7F40E2-F6CA-46AB-9DBC-46298D7E75CB',
                  name: 'N/A',
                  sort: 28,
                  faceUp: false
                },
                {
                  id: '251B1168-C05B-4980-B20E-98C0FC9B09FB',
                  name: 'N/A',
                  sort: 13,
                  faceUp: false
                },
                {
                  id: 'F489E0FD-9430-4052-BF39-90F940BDA055',
                  name: 'N/A',
                  sort: 9,
                  faceUp: false
                },
                {
                  id: 'CCDA64C6-BBB9-4E50-8D3B-19BB90CB20F7',
                  name: 'N/A',
                  sort: 21,
                  faceUp: false
                },
                {
                  id: '34CC5A81-C318-4AB5-B89D-9A4CAEDD295D',
                  name: 'N/A',
                  sort: 35,
                  faceUp: false
                },
                {
                  id: '5B65E26B-8B9F-44A2-A43D-F45A7C17224F',
                  name: 'N/A',
                  sort: 36,
                  faceUp: false
                },
                {
                  id: '0C427174-8A31-4FA7-AF96-DB4511E827E3',
                  name: 'N/A',
                  sort: 19,
                  faceUp: false
                },
                {
                  id: '512063CD-193A-401B-AF36-8D826FD5FADB',
                  name: 'N/A',
                  sort: 31,
                  faceUp: false
                },
                {
                  id: 'E5BE24F7-B359-4E9F-BD32-BC3E5E1FB73B',
                  name: 'N/A',
                  sort: 16,
                  faceUp: false
                },
                {
                  id: 'B5DA5764-6FC1-4BC7-B242-BA80907493E5',
                  name: 'N/A',
                  sort: 34,
                  faceUp: false
                },
                {
                  id: '3CD828D1-DA1C-4D60-95E7-954C8EF95733',
                  name: 'N/A',
                  sort: 30,
                  faceUp: false
                },
                {
                  id: '260729AC-2B54-4233-BD04-FCA89BA30DCD',
                  name: 'N/A',
                  sort: 8,
                  faceUp: false
                },
                {
                  id: '26D80657-C1C2-4D1A-884A-04D1C5DA30B9',
                  name: 'N/A',
                  sort: 17,
                  faceUp: false
                },
                {
                  id: '60BD37D2-6E2D-45E7-A1B8-9E2C4F665284',
                  name: 'N/A',
                  sort: 33,
                  faceUp: false
                },
                {
                  id: 'BF2FDE6A-6CB9-4AEE-8BDA-C7FC2C67705A',
                  name: 'N/A',
                  sort: 5,
                  faceUp: false
                },
                {
                  id: 'DD0E9F90-5ABA-4D3D-8D0E-7F5A91506C26',
                  name: 'N/A',
                  sort: 20,
                  faceUp: false
                },
                {
                  id: 'DFDBF595-8982-4B55-8706-5DA51A1D7ACD',
                  name: 'N/A',
                  sort: 25,
                  faceUp: false
                },
                {
                  id: 'BD6D4DA2-411F-4D67-BB3E-4B892952741A',
                  name: 'N/A',
                  sort: 2,
                  faceUp: false
                },
                {
                  id: '8E4E0819-F567-4262-8D39-D09668323F56',
                  name: 'N/A',
                  sort: 4,
                  faceUp: false
                },
                {
                  id: '97FABF6E-41EE-4D70-9B9B-051BAE760435',
                  name: 'N/A',
                  sort: 32,
                  faceUp: false
                },
                {
                  id: '9FBE2C53-E13E-414D-8A11-D821076C8C22',
                  name: 'N/A',
                  sort: 12,
                  faceUp: false
                },
                {
                  id: '102A1C97-5DBE-45C2-8490-5D3771318C81',
                  name: 'N/A',
                  sort: 23,
                  faceUp: false
                },
                {
                  id: '51314643-96AD-4551-BC4A-4F73AE804986',
                  name: 'N/A',
                  sort: 10,
                  faceUp: false
                },
                {
                  id: '717D15FC-46E8-4FB0-8D3C-C4A34C70ED50',
                  name: 'N/A',
                  sort: 18,
                  faceUp: false
                },
                {
                  id: '34B0C094-5E2B-4493-8363-5A68980B1246',
                  name: 'N/A',
                  sort: 11,
                  faceUp: false
                },
                {
                  id: 'DFA762CA-EE50-4110-8815-FC57612632AF',
                  name: 'N/A',
                  sort: 1,
                  faceUp: false
                }
              ]
            }
          ]
        }
      } as any as GameMessage), 1000);
  }

  private initMockWebsocketGameStartMessages() {
    setTimeout(
      () => this.handleWebsocketMessage({
        command: Action.START_GAME
      } as any as GameMessage), 1000);
  }

  private initMockWebsocketGameStartedMessages() {
    setTimeout(
      () => this.handleWebsocketMessage({
        command: Action.GAME_STATE,
        game: {
          id: 'EA9CA14C-AA81-4A62-8536-E68099975130',
          title: 'Jassrunde 0815',
          state: 'STARTED',
          players: [
            {
              id: '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD',
              name: 'Chefspieler',
              position: 1,
              hand: {
                id: '6355D74C-EAB6-49EA-AC78-EC6CE31486AE',
                cardCount: 9,
                cards: [
                  {
                    id: '5E576E4D-9E4F-453D-8441-0C0EFFE9C46E',
                    name: '8S',
                    sort: 30,
                    faceUp: true
                  },
                  {
                    id: 'B71D8946-2EC9-4039-BDE3-08E10A8BF786',
                    name: '9C',
                    sort: 4,
                    faceUp: true
                  },
                  {
                    id: '81F28F7A-E363-4DB4-9C4C-55C168DA0A50',
                    name: '10H',
                    sort: 14,
                    faceUp: true
                  },
                  {
                    id: 'BD7A137F-AC41-4622-B6FB-109FC20B7CA6',
                    name: '7C',
                    sort: 2,
                    faceUp: true
                  },
                  {
                    id: 'C6C44CC1-3687-4FD0-9214-E033E83A358C',
                    name: '6C',
                    sort: 1,
                    faceUp: true
                  },
                  {
                    id: '6B18DB7F-7808-4255-8D1C-0346F51F3A54',
                    name: '9S',
                    sort: 31,
                    faceUp: true
                  },
                  {
                    id: 'C53D4E87-9624-4B7A-9B13-DACAE306CBC3',
                    name: '7D',
                    sort: 20,
                    faceUp: true
                  },
                  {
                    id: '1C8AD07F-DB93-400C-8B77-2AB04D1F6656',
                    name: 'JS',
                    sort: 33,
                    faceUp: true
                  },
                  {
                    id: '8DD158E4-A10C-4CCC-9DB2-75653DAB1AEE',
                    name: 'AS',
                    sort: 36,
                    faceUp: true
                  }
                ]
              },
              stacks: [],
              organizer: true,
              playerReady: true
            },
            {
              id: '53BE4441-C575-41B9-BECD-9B2A634C771B',
              name: 'Spieler2',
              position: 2,
              hand: {
                id: '1224EDD4-411F-4E33-B497-8D636D153403',
                cardCount: 9,
                cards: []
              },
              stacks: [],
              organizer: false,
              playerReady: false
            },
            {
              id: '0EB0DD34-8DD9-44E6-8D21-9265E190500A',
              name: 'Spieler3',
              position: 3,
              hand: {
                id: 'C2EDDE97-B453-462B-B74B-BB615A1789CC',
                cardCount: 9,
                cards: []
              },
              stacks: [],
              organizer: false,
              playerReady: false
            },
            {
              id: '8EE3E68B-C9B8-41B2-BEC8-6BBE4916B817',
              name: 'Spieler4',
              position: 4,
              hand: {
                id: '97074BD3-49A2-48B8-BA1F-9F5139223F60',
                cardCount: 9,
                cards: []
              },
              stacks: [],
              organizer: false,
              playerReady: false
            }
          ],
          stacks: [
            {
              id: 'C22024A4-8DC2-4AF9-BA3E-AC91D5560B3F',
              cards: []
            }
          ]
        }
      } as any as GameMessage), 3000);
  }

  private initMockWebsocketGameCardAddedMessage(card: CardDTO) {
    setTimeout(
      () => {
        const game = {...this.gameSubject.getValue()};
        game.playedCards = {
          cards: [{...card, playerId: this.playerId}],
          onSameStack: false
        };
        this.handleWebsocketMessage({
          command: Action.GAME_STATE,
          game
        } as any as GameMessage);
      }, 1000);

    setTimeout(() => {
      const game = {...this.gameSubject.getValue()};
      game.playedCards.cards = [...game.playedCards.cards, {...card, playerId: game.players[1].id},
        {...card, playerId: game.players[2].id}, {...card, playerId: game.players[3].id}];
      this.handleWebsocketMessage({
        command: Action.GAME_STATE,
        game
      } as any as GameMessage);
    }, 2000);
  }
}
