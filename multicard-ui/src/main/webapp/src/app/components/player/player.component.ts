import {Component, Input, OnInit} from '@angular/core';
import {PlayerConfiguration} from '../../model/game.model';

const rotationPerCardInDegrees = 5;
const translationXPerCardInPixels = 7;

@Component({
  selector: 'mc-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  @Input()
  public player!: PlayerConfiguration;

  @Input()
  public turnNameAround = false;

  public numberOfCards = 10;

  constructor() {
  }

  ngOnInit(): void {
  }

  getRotation(i: number): number {
    return 0 - (Math.max(this.numberOfCards - 1, 0) * rotationPerCardInDegrees / 2) + i * rotationPerCardInDegrees;
  }

  getTranslateX(i: number): number {
    return 0 - (Math.max(this.numberOfCards - 1, 0) * translationXPerCardInPixels / 2) + i * translationXPerCardInPixels;
  }
}
