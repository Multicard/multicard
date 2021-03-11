import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';
import {Subject} from 'rxjs';
import {GameService} from '../../services/game.service';
import {takeUntil} from 'rxjs/operators';
import {CardDTO, StackDTO} from '../../../app-gen/generated-model';
import {ActionType, DirectionType, StackAction} from '../../model/game.model';
import {getCardImage} from '../../model/cardHelper';

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

  @Input() public stack!: StackDTO;
  translateExpression = '';
  private unsubscribe = new Subject();

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
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

  public getCardImage(card: CardDTO) {
    return getCardImage(card);
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
          if (this.stack.cards?.length > 0) {
            this.stack.cards.pop();
          }
        }, i * 20);
      }
    }
  }
}
