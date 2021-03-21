import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {GamePlayer, Player} from '../../../../model/game.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {PlayerService} from '../../../../services/player.service';
import {GameDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {GameService} from '../../../../services/game.service';
import {finalize} from 'rxjs/operators';

export interface PlayerRegistrationParam {
  isOrganizer: boolean;
  player: Player;
  game: GameDTO;
}

const ERROR_NO_SPACE_FOR_NEW_PLAYER = `Diesem Spiel sind bereits 4 Spieler*innen beigetreten und es können keine weiteren \
Spieler*innen mitspielen.
Bitte trage den korrekten Namen und das Passwort ein, falls du dich bereits für das Spiel registriert hast und weiterspielen möchtest.`;

const ERROR_WRONG_PASSWORD = 'Das eingegebene Passwort ist falsch.';

const ERROR_COMMUNICATION = 'Leider ist ein unerwarteter Fehler aufgetreten. Bitte versuche es noch einmal.';

@Component({
  selector: 'mc-player-registration-dialog',
  templateUrl: './player-registration-dialog.component.html',
  styleUrls: ['./player-registration-dialog.component.scss']
})
export class PlayerRegistrationDialogComponent implements OnInit {

  game: GameDTO;
  player: Player;
  organizerMode = false;
  password!: string;
  noFreePLayersLeft = false;
  isRestCallInProgress = false;
  errorMsgTop?: string;
  errorMsgBottom?: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: PlayerRegistrationParam,
    private dialogRef: MatDialogRef<PlayerRegistrationDialogComponent>,
    private fb: FormBuilder,
    private gameService: GameService,
    private playerService: PlayerService
  ) {
    this.game = data.game;
    this.player = data.player;
    this.organizerMode = data.isOrganizer;
  }

  ngOnInit(): void {
    if (this.game.players?.length >= 4) {
      this.noFreePLayersLeft = true;
      this.errorMsgTop = ERROR_NO_SPACE_FOR_NEW_PLAYER;
      return;
    }
  }

  getOkButtonText() {
    return this.organizerMode ? 'Mitspieler einladen' : 'Spiel beitreten';
  }

  okButtonClicked() {
    this.isRestCallInProgress = true;
    this.errorMsgTop = undefined;
    this.errorMsgBottom = undefined;
    if (!this.organizerMode) {
      const existingPLayer = this.game.players?.find(p => p.name === this.player.playerName);
      if (existingPLayer) {
        this.checkPassword(existingPLayer);
        return;
      } else if (this.noFreePLayersLeft) {
        this.isRestCallInProgress = false;
        this.errorMsgTop = ERROR_NO_SPACE_FOR_NEW_PLAYER;
        return;
      }
    }

    this.addPlayerToGame();
  }

  private checkPassword(player: PlayerDTO) {
    this.playerService.checkPassword(player.id, this.password)
      .pipe(finalize(() => this.isRestCallInProgress = false))
      .subscribe(result => {
        if (result) {
          this.savePlayerAndCloseDialog(player.id);
        } else {
          this.errorMsgBottom = ERROR_WRONG_PASSWORD;
        }
      }, e => {
        console.error('error on password check', e);
        this.errorMsgBottom = ERROR_COMMUNICATION;
      });
  }

  private addPlayerToGame() {
    const playerPos = this.game.players ? this.game.players.length + 1 : 1;
    this.gameService.addPlayer(this.game.id, this.organizerMode, this.player.playerName, playerPos, this.password)
      .pipe(finalize(() => this.isRestCallInProgress = false))
      .subscribe(player => {
        this.savePlayerAndCloseDialog(player.id);
      }, e => {
        console.error('error on adding player', e);
        this.errorMsgBottom = ERROR_COMMUNICATION;
      });
  }

  private savePlayerAndCloseDialog(playerId: string) {
    this.player.registeredGames.push(new GamePlayer(this.game.id, playerId));
    this.playerService.storePlayerInLocalStorage(this.player);
    this.dialogRef.close(this.player);
  }
}
