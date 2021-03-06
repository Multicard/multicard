import {Component, Input, OnInit} from '@angular/core';
import {Card} from '../../model/game.model';

@Component({
  selector: 'mc-card-pile',
  templateUrl: './card-pile.component.html',
  styleUrls: ['./card-pile.component.scss']
})
export class CardPileComponent implements OnInit {

  @Input()
  public cards?: Card[] = [];

  @Input()
  public cardCssClass = '';

  constructor() {
  }

  ngOnInit(): void {
  }

  public get3DMargin(i: number): number {
    return 2 * i;
  }

  public getCardImage(card: Card) {
    return 'assets/cards/' + (card.isFaceUp ? card.card : 'BLUE_BACK') + '.svg';
  }
}
