import {Component, Input, OnInit} from '@angular/core';
import {PlayerConfiguration} from '../../model/game.model';

@Component({
  selector: 'mc-user-player',
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit {

  @Input()
  public player!: PlayerConfiguration;

  public cards = ['AS', '3C', '10H', 'JC', '7D', 'QD', 'KS', 'AH', '5C', '9D'];

  constructor() {
  }

  ngOnInit(): void {
  }

}
