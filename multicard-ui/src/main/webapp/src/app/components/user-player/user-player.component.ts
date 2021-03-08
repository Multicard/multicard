import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CardDTO, PlayerDTO} from '../../../app-gen/generated-model';
import {createCardsForHand, getCardImage} from '../../model/cardHelper';

@Component({
  selector: 'mc-user-player',
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit, OnChanges {

  @Input()
  public player!: PlayerDTO;

  public handCards: CardDTO[] = [];

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.handCards = createCardsForHand(this.player?.hand);
  }

  public getCardImage(card: CardDTO) {
    return getCardImage(card);
  }
}
