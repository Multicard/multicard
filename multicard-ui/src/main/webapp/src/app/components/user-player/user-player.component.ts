import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Card, Player} from '../../model/game.model';
import {createCardsForStack} from '../../model/cardHelper';

@Component({
  selector: 'mc-user-player',
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit, OnChanges {

  @Input()
  public player!: Player;

  public cards: Card[] = [];

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.cards = createCardsForStack(this.player?.stacks[0]);
  }

  getCards() {
    if (this.player?.hand?.cards !== undefined) {
      return this.player?.hand?.cards;
    } else {
      return new Array(this.player?.hand?.numberOfCards).fill('BLUE_BACK');
    }
  }
}
