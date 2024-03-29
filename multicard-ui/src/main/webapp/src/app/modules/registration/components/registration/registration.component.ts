import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MatButton} from '@angular/material/button';
import {MatDialog} from '@angular/material/dialog';
import {
  PlayerRegistrationDialogComponent,
  PlayerRegistrationParam
} from '../player-registration-dialog/player-registration-dialog.component';
import {PlayerService} from '../../../../services/player.service';
import {Player} from '../../../../model/game.model';
import {GameService} from '../../../../services/game.service';
import {finalize} from 'rxjs/operators';

const MISSING_GAME_ID = `Gib bitte die ID des Spiels ein, an welchem du teilnehmen willst.
Kontaktiere den Spielorganisator, falls du sie nicht kennst`;
const UNKNOWN_GAME_ID = `Die eingegene Spiel ID ist unbekannt. Bitte prüfe den eingegebenen Wert und
kontaktiere den Spielorganisator, falls du die Spiel ID richtig eingegeben hast`;
const COMMUNICATION_ERROR_MSG = 'Leider ist ein unerwarteter Fehler aufgetreten. Bitte versuche es noch einmal.';

@Component({
  selector: 'mc-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  @ViewChild('createGameBtn', {static: true}) buttonRef!: MatButton;
  @ViewChild('errorDialogTemplate', {static: true}) errorDialogTemplate!: TemplateRef<any>;

  gameId!: string;
  player!: Player;
  gameLoadingInProgress = false;

  constructor(
    private gameService: GameService,
    private playerService: PlayerService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.player = this.playerService.loadPlayerFromLocalStorage();
    this.buttonRef.focus();
    this.route.paramMap.subscribe(p => {
      const pGameId = p.get('gameId');
      if (pGameId) {
        this.gameId = pGameId;
        this.joinGame();
      }
    });
  }

  joinGame() {
    if (!this.gameId) {
      this.openErrorDialog(MISSING_GAME_ID);
      return;
    }

    this.gameLoadingInProgress = true;
    this.gameService.loadGame(this.gameId)
      .pipe(finalize(() => this.gameLoadingInProgress = false))
      .subscribe(foundGame => {
        if (!foundGame) {
          this.openErrorDialog(UNKNOWN_GAME_ID);
          return;
        }
        const playerId = this.player.registeredGames.find(g => g.gameId === this.gameId)?.playerId;
        const isRelogin = playerId !== undefined && foundGame.players?.find(p => p.id === playerId) !== undefined;
        const data: PlayerRegistrationParam = {isOrganizer: false, isRelogin, game: foundGame, player: this.player};
        const dialogRef = this.dialog.open(PlayerRegistrationDialogComponent,
          {data, hasBackdrop: true, disableClose: true, position: {top: '50px'}});
        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            this.handlePlayerRegistrationResult(result as Player);
          }
        });
      }, e => {
        console.error('error while loading game', e);
        this.openErrorDialog(COMMUNICATION_ERROR_MSG);
      });
  }

  createGame() {
    this.router.navigate(['/registration/config']);
  }

  private handlePlayerRegistrationResult(player: Player) {
    const playerId = this.findPLayerIdForGame(player)?.playerId;
    if (playerId) {
      this.navigateToGame(playerId);
    } else {
      console.error('playerId is not defined after player registration', player);
    }
  }

  private findPLayerIdForGame(player: Player) {
    return player.registeredGames.find(game => game.gameId === this.gameId);
  }

  private navigateToGame(playerId: string) {
    this.router.navigate([`/game/${this.gameId}/${playerId}`]);
  }

  private openErrorDialog(error: string) {
    this.dialog.open(this.errorDialogTemplate, {data: {error}, position: {top: '60px'}});
  }
}
