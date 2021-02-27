import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {ActionType, Card, DirectionType, Stack, StackAction} from '../../model/game.model';
import {Subject} from 'rxjs';
import {GameService} from '../../services/game.service';
import {takeUntil} from 'rxjs/operators';

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
export class StackComponent implements OnInit, OnDestroy {

  @Input()
  public stack!: Stack;

  public cards: Card[] = [];

  translateExpression = '';

  private unsubscribe = new Subject();

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.cards = new Array(this.stack.numberOfCards).fill(new Card());
    if (this.stack.isFaceUp) {
      const topCard = this.cards[this.stack.numberOfCards - 1];
      topCard.isFaceUp = true;
      topCard.card = this.stack.topCard;
    }

    this.gameService.registerStackObserver()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((action) => this.triggerAction(action));
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
  }

  public get3DMargin(i: number): number {
    return 2 * Math.round(i / 4);
  }

  public getCardImage() {
    return 'assets/cards/' + (this.stack.isFaceUp ? this.stack.topCard : 'BLUE_BACK.svg');
  }

  private triggerAction(action: StackAction) {
    if (action.action === ActionType.drawCard) {
      switch (action.direction) {
        case DirectionType.down:
          this.translateExpression = 'translatey(50vh) rotate(180deg)';
          break;
        case DirectionType.left:
          this.translateExpression = 'translatex(-40vw) rotate(90deg)';
          break;
        case DirectionType.up:
          this.translateExpression = 'translatey(-30vh) rotate(180deg)';
          break;
        case DirectionType.right:
        default:
          this.translateExpression = 'translatex(40vw) rotate(90deg)';
      }
      for (let i = 0; i < action.numberOfCards; i++) {
        setTimeout(() => {
          if (this.cards.length > 0) {
            this.cards.pop();
          }
        }, i * 20);
      }
    }
  }
}
