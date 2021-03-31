import {ChangeDetectionStrategy, Component, OnDestroy, OnInit} from '@angular/core';
import {GameService} from '../../../../services/game.service';
import {Observable} from 'rxjs';
import {GameDTO, Gamestate} from '../../../../../app-gen/generated-model';
import {ActivatedRoute} from '@angular/router';
import {tap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog, MatDialogRef, MatDialogState} from '@angular/material/dialog';
import {
  UncoveredCardsDialogComponent,
  UncoveredCardsReturnType
} from '../uncovered-cards/uncovered-cards-dialog.component';
import {ScoreBoardDialogComponent} from '../score-board/score-board-dialog.component';

@Component({
  selector: 'mc-game',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  gameState$!: Observable<GameDTO>;
  private numberOfPlayers = 0;
  private uncoveredCardsDialogRef?: MatDialogRef<any, UncoveredCardsReturnType>;
  private scoreBoardDialogRef?: MatDialogRef<any, any>;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(p => {
      const gameId = p.get('gameId');
      const playerId = p.get('playerId');
      if (gameId !== null && playerId !== null) {
        this.gameState$ = this.gameService.initWebsocketCommunication(gameId, playerId)
          .pipe(
            tap<GameDTO>(game => {
              this.handleGameStateChanges(game);
            })
          );
      } else {
        console.error('gameId or playerId is not set', this.route);
      }
    });
  }

  ngOnDestroy(): void {
    this.gameService.closeGame();
  }

  isGameStarteable(game: GameDTO) {
    return game?.state === Gamestate.READYTOSTART
      //&& game.players[0]?.organizer
      && game?.players?.length >= 4;
  }

  isGameEndable(game: GameDTO) {
    return game?.state === Gamestate.STARTED
      //&& game.players[0]?.organizer
      && game?.playedCards && game.playedCards?.cards && game.playedCards.cards.length === 0
      && game.players.reduce((unplayedCards, player) => unplayedCards + (player?.hand?.cards?.length || 0), 0) === 0;
  }

  isRoundAbortable(game: GameDTO) {
    return game?.state === Gamestate.STARTED;
  }

  startGame() {
    this.gameService.startRound();
  }

  endRound() {
    this.gameService.endRound();
  }

  abortRound() {
    this.gameService.startNewRound();
  }

  showScore() {
    this.dialog.open(ScoreBoardDialogComponent,
      {data: this.gameState$, hasBackdrop: true});
  }

  private handleGameStateChanges(game: GameDTO) {
    const oldNumberOfPlayers = this.numberOfPlayers;
    const newNumberOfPlayer = game?.players ? game.players.length : 0;

    if (newNumberOfPlayer === 1) {
      this.showMessage('Es sind noch keine weiteren Spieler*innen registriert');
    }

    if (oldNumberOfPlayers >= 1 && newNumberOfPlayer >= 2 && game.players.length > oldNumberOfPlayers) {
      const newPlayer = game.players.reduce((acc, cur) => acc.position > cur.position ? acc : cur);
      this.showMessage(`${newPlayer.name} hat sich im Spiel registriert`);
    }

    if (game?.currentRound <= 1 && oldNumberOfPlayers < 4 && this.isGameStarteable(game) && game.players[0]?.organizer) {
      setTimeout(() =>
        this.showMessage(`Durch das Verschieben der Spieler*innen kann die Positionierung am Spieltisch geändert werden.`), 4000);
    }

    // öffne den aufgedeckte Karten Dialog (falls nicht bereits geöffnet)
    if (game?.state === Gamestate.ROUND_ENDED
      && (this.uncoveredCardsDialogRef === undefined || this.uncoveredCardsDialogRef.getState() !== MatDialogState.OPEN)) {

      this.uncoveredCardsDialogRef = this.dialog.open(UncoveredCardsDialogComponent,
        {data: this.gameState$, hasBackdrop: true, disableClose: true});
      this.uncoveredCardsDialogRef.afterClosed().subscribe((retVal) => {
        if (retVal?.initiateNewRound) {
          this.gameService.startNewRound();
        } else if (retVal?.endGame) {
          this.gameService.endGame();
        }
      });
    }
    // schliesse den aufgedeckte Karten Dialog, falls der GameState von einem anderen Spieler geändert wurde
    else if (game?.state !== Gamestate.ROUND_ENDED && this.uncoveredCardsDialogRef !== undefined) {
      this.uncoveredCardsDialogRef.close();
    }

    // öffne den Spieltafel Dialog (falls nicht bereits geöffnet)
    if (game?.state === Gamestate.GAME_ENDED
      && (this.scoreBoardDialogRef === undefined || this.scoreBoardDialogRef.getState() !== MatDialogState.OPEN)) {

      this.scoreBoardDialogRef = this.dialog.open(ScoreBoardDialogComponent,
        {data: this.gameState$, hasBackdrop: false, disableClose: true});
    }
    // schliesse den Spieltafel Dialog, falls der GameState von einem anderen Spieler geändert wurde
    else if (game?.state !== Gamestate.GAME_ENDED && this.scoreBoardDialogRef !== undefined) {
      this.scoreBoardDialogRef.close();
    }

    this.numberOfPlayers = game?.players ? game.players.length : 0;
  }

  private showMessage(message: string, duration = 4000) {
    this.snackBar.open(message, 'X', {
      duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
}
