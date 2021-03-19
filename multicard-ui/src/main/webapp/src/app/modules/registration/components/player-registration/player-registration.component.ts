import {Component, Inject, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {GamePlayer, Player} from '../../../../model/game.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {PlayerService} from '../../../../services/player.service';

export interface GamePlayerParam {
  gameId: string;
  player: Player;
}

const ERROR_NO_PLACE_FOR_NEW_PLAYER = `Diesem Spiel sind bereits 4 Spieler beigetreten und es können keine weiteren Spieler mitspielen.
Falls du dich bereits für das Spiel registriert hast und weiterspielen möchtest, musst du den korrekten Spielernamen eintragen.`;

@Component({
  selector: 'mc-player-registration',
  templateUrl: './player-registration.component.html',
  styleUrls: ['./player-registration.component.scss']
})
export class PlayerRegistrationComponent implements OnInit, OnChanges {

  player: Player;
  password!: string;
  errorMsg?: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: GamePlayerParam,
    private dialogRef: MatDialogRef<PlayerRegistrationComponent>,
    private fb: FormBuilder,
    private playerService: PlayerService
  ) {
    this.player = data.player;
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.errorMsg = undefined;
  }

  get gameId(): string {
    return this.data.gameId;
  }

  enterGame() {
    if (this.gameId) {
      // TODO registriere den Player im Game (REST call)
      // Fehlerhandling, falls Registrierung nok
      // TODO löschen
      const players = [
        {id: '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD', name: 'Chefspieler'},
        {id: '53BE4441-C575-41B9-BECD-9B2A634C771B', name: 'Spieler2'},
        {id: '0EB0DD34-8DD9-44E6-8D21-9265E190500A', name: 'Spieler3'},
        {id: '8EE3E68B-C9B8-41B2-BEC8-6BBE4916B817', name: 'Spieler4'}
      ];
      const playerId = players.find(p => p.name === this.player.playerName)?.id;
      if (playerId === undefined) {
        this.errorMsg = ERROR_NO_PLACE_FOR_NEW_PLAYER;
        return;
      }
      this.player.registeredGames.push(new GamePlayer(this.gameId, playerId));
      this.savePlayerAndCloseDialog();
    } else {
      this.savePlayerAndCloseDialog();
    }
  }

  private savePlayerAndCloseDialog() {
    this.playerService.storePlayerInLocalStorage(this.player);
    this.dialogRef.close(this.player);
  }
}
