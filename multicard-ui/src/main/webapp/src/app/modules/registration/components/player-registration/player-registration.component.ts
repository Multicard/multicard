import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GameDTO} from '../../../../../app-gen/generated-model';
import {GameService} from '../../../../services/game.service';
import {MatDialog} from '@angular/material/dialog';
import {Player} from '../../../../model/game.model';
import {PlayerService} from '../../../../services/player.service';
import {
  PlayerRegistrationDialogComponent,
  PlayerRegistrationParam
} from '../player-registration-dialog/player-registration-dialog.component';

const ERROR_MSG = 'Leider ist ein unerwarteter Fehler aufgetreten. Bitte lade die Seite neu.';

@Component({
  selector: 'mc-player-registration',
  templateUrl: './player-registration.component.html',
  styleUrls: ['./player-registration.component.scss']
})
export class PlayerRegistrationComponent implements OnInit {

  @ViewChild('errorDialogTemplate', {static: true}) errorDialogTemplate!: TemplateRef<any>;

  player!: Player;
  gameId!: string;
  game!: GameDTO;

  constructor(
    private playerService: PlayerService,
    private gameService: GameService,
    private route: ActivatedRoute,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.player = this.playerService.loadPlayerFromLocalStorage();
    this.route.paramMap.subscribe(p => {
      const gameId = p.get('gameId');
      if (gameId) {
        this.gameId = gameId;
        this.loadGameAndCreateOrganizer(this.gameId);
      } else {
        console.error('gameId or playerId is not set', this.route);
      }
    });
  }

  private loadGameAndCreateOrganizer(gameId: string) {
    this.gameService.loadGame(gameId).subscribe(game => {
      this.game = game;
      if (!game.players || !game.players.find(p => p.organizer)) {
        this.createOrganizerPlayer(game);
      }
    }, e => {
      console.error('error on game creation', e);
      this.dialog.open(this.errorDialogTemplate, {data: {error: ERROR_MSG}, position: {top: '60px'}});
    });
  }

  private createOrganizerPlayer(game: GameDTO) {
    const data: PlayerRegistrationParam = {isOrganizer: true, game, player: this.player};
    const dialogRef = this.dialog.open(PlayerRegistrationDialogComponent,
      {data, hasBackdrop: false, position: {top: '100px'}});
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        //this.handlePlayerRegistrationResult(result as Player);
      }
    });
  }
}
