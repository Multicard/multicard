import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {Subject} from 'rxjs';
import {GameService} from '../../../../services/game.service';
import {takeUntil} from 'rxjs/operators';
import {CardDTO, StackDTO} from '../../../../../app-gen/generated-model';
import {ActionType, DirectionType, StackAction} from '../../../../model/game.model';
import {getCardImage} from '../../../../model/cardHelper';

@Component({
  selector: 'mc-stack',
  changeDetection: ChangeDetectionStrategy.OnPush,
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

  @Input() public stack!: StackDTO;
  translateExpression = '';
  private unsubscribe = new Subject();

  constructor(
    private gameService: GameService,
    private changeDetectorRef: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.gameService.registerStackObserver()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe((action) => this.triggerAction(action));
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
  }

  get3DMargin(i: number): number {
    return 2 * Math.round(i / 4);
  }

  getCardImage(card: CardDTO) {
    return getCardImage(card);
  }

  trackByCardId(index: number, card: CardDTO) {
    return card.id;
  }

  private triggerAction(action: StackAction) {
    if (action.action === ActionType.drawCard) {
      switch (action.direction) {
        case DirectionType.down:
          this.translateExpression = 'translateY(30vh) rotate(180deg)';
          break;
        case DirectionType.left:
          this.translateExpression = 'translateX(-40vw) rotate(90deg)';
          break;
        case DirectionType.up:
          this.translateExpression = 'translateY(-30vh) rotate(180deg)';
          break;
        case DirectionType.right:
        default:
          this.translateExpression = 'translateX(40vw) rotate(90deg)';
      }
      for (let i = 0; i < action.numberOfCards; i++) {
        setTimeout(() => {
          if (this.stack.cards?.length > 0) {
            this.stack.cards.pop();
            this.changeDetectorRef.detectChanges();
          }
        }, i * 20);
      }
    }
  }
}
