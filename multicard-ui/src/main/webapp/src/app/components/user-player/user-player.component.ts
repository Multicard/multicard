import {Component, Input, OnInit} from '@angular/core';
import {Player} from '../../model/game.model';

@Component({
  selector: 'mc-user-player',
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit {

  @Input()
  public player!: Player;

  constructor() {
  }

  ngOnInit(): void {
  }

  getCards() {
    if (this.player?.hand?.cards !== undefined) {
      return this.player?.hand?.cards;
    } else {
      return new Array(this.player?.hand?.numberOfCards).fill('BLUE_BACK');
    }
  }

}
