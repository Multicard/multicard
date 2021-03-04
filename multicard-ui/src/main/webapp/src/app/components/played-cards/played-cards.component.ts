import {Component, Input, OnInit} from '@angular/core';
import {PlayedCards} from '../../model/game.model';
import {CdkDragDrop} from '@angular/cdk/drag-drop';

@Component({
  selector: 'mc-played-cards',
  templateUrl: './played-cards.component.html',
  styleUrls: ['./played-cards.component.scss']
})
export class PlayedCardsComponent implements OnInit {

  @Input()
  public playedCards?: PlayedCards;

  @Input()
  public playerIds!: string[];

  constructor() { }

  ngOnInit(): void {
  }

  cardDroppedBottom(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet in bottom area');
  }
}
