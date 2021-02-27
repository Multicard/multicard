import {Component, Input, OnInit} from '@angular/core';
import {Player} from '../../model/game.model';

const rotationPerCardInDegrees = 5;
const translationXPerCardInPixels = 7;

@Component({
  selector: 'mc-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  @Input()
  public player!: Player;

  @Input()
  public turnNameAround = false;

  constructor() {
  }

  ngOnInit(): void {
  }

  getCards() {
    return new Array(this.getNumberOfCards());
  }

  getRotation(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * rotationPerCardInDegrees / 2) + i * rotationPerCardInDegrees;
  }

  getTranslateX(i: number): number {
    return 0 - (Math.max(this.getNumberOfCards() - 1, 0) * translationXPerCardInPixels / 2) + i * translationXPerCardInPixels;
  }

  private getNumberOfCards() {
    return this.player?.hand?.numberOfCards;
  }
}
