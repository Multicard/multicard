import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CardDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {createCardsForHand} from '../../../../model/cardHelper';

const rotationPerCardInDegrees = 5;
const translationXPerCardInPixels = 7;

@Component({
  selector: 'mc-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit, OnChanges {

  @Input() public player!: PlayerDTO;
  @Input() public turnNameAround = false;
  public handCards: CardDTO[] = [];

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.handCards = createCardsForHand(this.player?.hand);
  }

  getRotation(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * rotationPerCardInDegrees / 2) + i * rotationPerCardInDegrees;
  }

  getTranslateX(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * translationXPerCardInPixels / 2) + i * translationXPerCardInPixels;
  }

  private getNumberOfCards() {
    return this.handCards?.length;
  }
}
