import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {CardDTO} from '../../../../../app-gen/generated-model';
import {animate, style, transition, trigger} from '@angular/animations';
import {getCardImage} from '../../../../model/cardHelper';

export enum FlyInAnimation {
  noAnimation = 'noAnimation',
  fromTop = 'fromTop',
  fromRight = 'fromRight',
  fromBottom = 'fromBottom',
  fromLeft = 'fromLeft'
}

@Component({
  selector: 'mc-card-pile',
  changeDetection: ChangeDetectionStrategy.OnPush,
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
    return getCardImage(card);
  }

  isAnimationDisabled(cardIndex: number) {
    // @ts-ignore
    return this.flyInAnimation === FlyInAnimation.noAnimation || cardIndex < this.cards.length - 1;
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

  trackByCardId(index: number, card: CardDTO) {
    return card.id;
  }
}
