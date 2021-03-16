import {ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CardDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {createCardsForHand} from '../../../../model/cardHelper';
import {FlyInAnimation} from '../card-pile/card-pile.component';

const rotationPerCardInDegrees = 5;
const translationXPerCardInPixels = 7;

export enum TablePosition {
  left,
  top,
  right,
  bottom
}

@Component({
  selector: 'mc-player',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit, OnChanges {

  @Input() player!: PlayerDTO;
  @Input() tablePosition = TablePosition.bottom;
  handCards: CardDTO[] = [];
  stack!: CardDTO[];

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.handCards = createCardsForHand(this.player?.hand);
    this.stack = this.player?.stacks?.length > 0 ? this.player.stacks[0]?.cards : [];
  }

  getRotation(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * rotationPerCardInDegrees / 2) + i * rotationPerCardInDegrees;
  }

  getTranslateX(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * translationXPerCardInPixels / 2) + i * translationXPerCardInPixels;
  }

  isNameTurnedAround() {
    return this.tablePosition === TablePosition.top;
  }

  getStackFlyInAnimation() {
    switch (this.tablePosition) {
      case TablePosition.left:
        return FlyInAnimation.fromLeft;
      case TablePosition.top:
        return FlyInAnimation.fromTop;
      case TablePosition.right:
        return FlyInAnimation.fromRight;
      case TablePosition.bottom:
        return FlyInAnimation.noAnimation;
    }
  }

  trackByCardId(index: number, card: CardDTO) {
    return card.id;
  }

  private getNumberOfCards() {
    return this.handCards?.length;
  }
}
