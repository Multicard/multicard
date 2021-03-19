import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {MatButton} from '@angular/material/button';
import {MatDialog} from '@angular/material/dialog';
import {GamePlayerParam, PlayerRegistrationComponent} from '../player-registration/player-registration.component';
import {PlayerService} from '../../../../services/player.service';
import {Player} from '../../../../model/game.model';

const MISSING_GAME_ID = `Gib bitte die ID des Spiels ein, an welchem du teilnehmen willst.
Kontaktiere den Spielorganisator, falls du sie nicht kennst`;
const UNKNOWN_GAME_ID = `Die eingegene Spiel ID ist unbekannt. Bitte prüfe den eingegebenen Wert und
kontaktiere den Spielorganisator, falls du die Spiel ID richtig eingegeben hast`;

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

  constructor(
    private playerService: PlayerService,
    private router: Router,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.player = this.playerService.loadPlayerFromLocalStorage();
    this.buttonRef.focus();
  }

  joinGame() {
    if (!this.gameId) {
      this.dialog.open(this.errorDialogTemplate, {data: {error: MISSING_GAME_ID}, position: {top: '60px'}});
      return;
    }

    const gameId = 'EA9CA14C-AA81-4A62-8536-E68099975130';
    if (this.gameId !== gameId) {
      this.dialog.open(this.errorDialogTemplate, {data: {error: UNKNOWN_GAME_ID}, position: {top: '60px'}});
      return;
    }

    const playerId = this.player.registeredGames.find(game => game.gameId === this.gameId)?.playerId;
    if (playerId !== undefined) {
      // TODO prüfen, dass der Player im Backend dem Spiel wirklich zugeordnet ist
      this.navigateToGame(playerId);
    } else {
      const data: GamePlayerParam = {gameId: this.gameId, player: this.player};
      const dialogRef = this.dialog.open(PlayerRegistrationComponent,
        {data, hasBackdrop: false, position: {top: '100px'}});
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.handlePlayerRegistrationResult(result as Player);
        }
      });
    }
  }

  createGame() {
    this.router.navigate(['/registration/config']);
  }

  private handlePlayerRegistrationResult(player: Player) {
    const playerId = this.findPLayerIdForGame(this.player)?.playerId;
    if (playerId) {
      this.navigateToGame(playerId);
    } else {
      console.error('playerId is not defined aftter player registration', player);
    }
  }
  private findPLayerIdForGame(player: Player) {
    return player.registeredGames.find(game => game.gameId === this.gameId);
  }

  private navigateToGame(playerId: string) {
    this.router.navigate([`/game/${this.gameId}/${playerId}`]);
  }
}
