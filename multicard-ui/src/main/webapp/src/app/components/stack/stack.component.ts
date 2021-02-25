import {Component, Input, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {Card, Stack} from '../../model/game.model';

@Component({
  selector: 'mc-stack',
  templateUrl: './stack.component.html',
  styleUrls: ['./stack.component.scss'],
  animations: [
    trigger('flyOut', [
      transition(':leave', [
        animate(400, style({transform: '{{translateExpression}}'})),
      ])
    ])
  ]
})
export class StackComponent implements OnInit {

  @Input()
  public stack!: Stack;

  public cards: Card[] = [];

  constructor() {
  }

  ngOnInit(): void {
    this.cards = new Array(this.stack.numberOfCards).fill(new Card());
    if (this.stack.isFaceUp) {
      const topCard = this.cards[this.stack.numberOfCards - 1];
      topCard.isFaceUp = true;
      topCard.card = this.stack.topCard;
    }
  }

  public get3DMargin(i: number): number {
    return 2 * Math.round(i / 4);
  }

  public onStackClick(): void {
    if (this.cards.length > 0) {
      this.cards.pop();
    }
  }

  public getFlyOutTranslateExpression(i: number) {
    // TODO remove dummy code
    switch (i % 4) {
      case 0:
        return 'translatey(50vh) rotate(180deg)';
      case 1:
        return 'translatex(-50vh) rotate(90deg)';
      case 2:
        return 'translatey(-30vh) rotate(180deg)';
      case 3:
      default:
        return 'translatex(50vh) rotate(90deg)';
    }
  }
}
