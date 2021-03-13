import {Component, Input, OnInit} from '@angular/core';
import {CardDTO} from '../../../../../app-gen/generated-model';
import {animate, style, transition, trigger} from '@angular/animations';

export enum FlyInAnimation {
  noAnimation = 'noAnimation',
  fromTop = 'fromTop',
  fromRight = 'fromRight',
  fromBottom = 'fromBottom',
  fromLeft = 'fromLeft'
}

@Component({
  selector: 'mc-card-pile',
  templateUrl: './card-pile.component.html',
  styleUrls: ['./card-pile.component.scss'],
  animations: [
    trigger('flyIn', [
      transition(':enter', [
        style({transform: '{{styleTransformExpression}}'}),
        animate('400ms ease-in', style({transform: '{{animateTransformExpression}}'})),
      ])
    ])
  ]
})
export class CardPileComponent implements OnInit {

  @Input() public cards?: CardDTO[] = [];
  @Input() public cardCssClass = '';
  @Input() public flyInAnimation = FlyInAnimation.noAnimation;

  constructor() {
  }

  ngOnInit(): void {
  }

  get3DMargin(i: number): number {
    const singleMargin = this.cards && this.cards.length > 10 ? 0.5 : 1;
    return singleMargin * i;
  }

  getCardImage(card: CardDTO) {
    return 'assets/cards/' + (card.faceUp ? card.name : 'BLUE_BACK') + '.svg';
  }

  isAnimationDisabled() {
    return this.flyInAnimation === FlyInAnimation.noAnimation;
  }

  getFlyInAnimateTransformExpression() {
    switch (this.flyInAnimation) {
      case FlyInAnimation.fromLeft:
      case FlyInAnimation.fromRight:
        return 'translateX(0)';

      default:
        return 'translateY(0)';
    }
  }

  getFlyInStyleTransformExpression() {
    switch (this.flyInAnimation) {
      case FlyInAnimation.fromLeft:
        return 'translateX(-40vw)';
      case FlyInAnimation.fromTop:
        return 'translateY(-30vh)';
      case FlyInAnimation.fromRight:
        return 'translateX(40vw)';
      default:
        return 'translateY(30vh)';
    }
  }
}
