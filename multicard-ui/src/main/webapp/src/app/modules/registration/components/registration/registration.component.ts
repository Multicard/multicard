import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'mc-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  readonly games = [{id: 'EA9CA14C-AA81-4A62-8536-E68099975130', name: 'Jassrunde 0815'}];
  readonly players = [
    {id: '45BC9F58-51D0-44D4-9E66-DD40C8C2B2BD', name: 'Chefspieler'},
    {id: '53BE4441-C575-41B9-BECD-9B2A634C771B', name: 'Spieler2'},
    {id: '0EB0DD34-8DD9-44E6-8D21-9265E190500A', name: 'Spieler3'},
    {id: '8EE3E68B-C9B8-41B2-BEC8-6BBE4916B817', name: 'Spieler4'}
  ];

  selectedGame!: string;
  selectedPlayer!: string;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  joinGame() {
    if (this.selectedGame === undefined || this.selectedPlayer === undefined) {
      alert('wähle bitte ein Spiel und einen Spieler aus');
    }

    this.router.navigate([`/game/${this.selectedGame}/${this.selectedPlayer}`]);
  }
}
