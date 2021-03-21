import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {GameService} from '../../../../services/game.service';
import {finalize} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';

const ERROR_MSG = 'Leider ist ein unerwarteter Fehler aufgetreten. Bitte wiederhole deine Aktion.';

@Component({
  selector: 'mc-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {

  @ViewChild('errorDialogTemplate', {static: true}) errorDialogTemplate!: TemplateRef<any>;

  gameForm = this.fb.group({
    gameName: ['', Validators.required],
    numberOfPlayers: [4],
    gameType: ['Schieber'],
    numberOfGivenCardsPerPlayer: [9],
    oneSharedStack: [true]
  });

  gameTypes: string[] = ['Schieber', 'Tschou Sepp', 'frei konfigurierbar'];

  isGameCreationInProgress = false;

  constructor(
    private fb: FormBuilder,
    private gameService: GameService,
    private router: Router,
    private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  isGameTypeSupported() {
    return this.gameForm.controls.gameType.value === 'Schieber';
  }

  isGameSpecificConfigurationVisible() {
    return this.gameForm.controls.gameType.value === 'frei konfigurierbar';
  }

  createGame() {
    this.isGameCreationInProgress = true;
    this.gameService.createGame(this.gameForm.controls.gameName.value)
      .pipe(finalize(() => this.isGameCreationInProgress = false))
      .subscribe(createdGame => {
        this.router.navigate([`/registration/player/${createdGame.id}`]);
      }, e => {
        console.error('error on game creation', e);
        this.dialog.open(this.errorDialogTemplate, {data: {error: ERROR_MSG}, position: {top: '60px'}});
      });
  }
}
