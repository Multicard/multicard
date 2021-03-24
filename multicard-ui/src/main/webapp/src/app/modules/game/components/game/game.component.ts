import {ChangeDetectionStrategy, Component, OnDestroy, OnInit} from '@angular/core';
import {GameService} from '../../../../services/game.service';
import {Observable} from 'rxjs';
import {GameDTO, Gamestate} from '../../../../../app-gen/generated-model';
import {ActivatedRoute} from '@angular/router';
import {tap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'mc-game',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  gameState$!: Observable<GameDTO>;
  private numberOfPlayers = 0;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService,
    private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(p => {
      const gameId = p.get('gameId');
      const playerId = p.get('playerId');
      if (gameId !== null && playerId !== null) {
        this.gameState$ = this.gameService.initGame(gameId, playerId)
          .pipe(
            tap<GameDTO>(game => {
              this.showMessagesOnStateChanges(game);
              this.numberOfPlayers = game?.players ? game.players.length : 0;
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
    return game?.state === Gamestate.READYTOSTART && game?.players?.length >= 4 && game.players[0]?.organizer;
  }

  startGame() {
    this.gameService.startGame();
  }

  private showMessagesOnStateChanges(game: GameDTO) {
    const oldNumberOfPlayers = this.numberOfPlayers;
    const newNumberOfPlayer = game?.players ? game.players.length : 0;
    if (newNumberOfPlayer === 1) {
      this.showMessage('Es sind noch keine weiteren Spieler*innen registriert');
    }
    if (oldNumberOfPlayers >= 1 && newNumberOfPlayer >= 2 && game.players.length > oldNumberOfPlayers) {
      this.showMessage(`${game.players[game.players.length -1].name} hat sich im Spiel registriert`);
    }
    if (oldNumberOfPlayers < 4 && this.isGameStarteable(game)) {
      this.showMessage(`Durch das Verschieben der Spieler*innen kann die Positionierung am Spieltisch geändert werden.`);
    }
  }

  private showMessage(message: string, duration = 4000) {
    this.snackBar.open(message, 'X', {
      duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }
}
