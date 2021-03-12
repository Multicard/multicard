import {Component, Input, OnInit} from '@angular/core';
import {CardDTO} from '../../../../../app-gen/generated-model';

@Component({
  selector: 'mc-card-pile',
  templateUrl: './card-pile.component.html',
  styleUrls: ['./card-pile.component.scss']
})
export class CardPileComponent implements OnInit {

  @Input() public cards?: CardDTO[] = [];
  @Input() public cardCssClass = '';

  constructor() {
  }

  ngOnInit(): void {
  }

  public get3DMargin(i: number): number {
    const singleMargin = this.cards && this.cards.length > 10 ? 0.5 : 1;
    return singleMargin * i;
  }

  public getCardImage(card: CardDTO) {
    return 'assets/cards/' + (card.faceUp ? card.name : 'BLUE_BACK') + '.svg';
  }
}
