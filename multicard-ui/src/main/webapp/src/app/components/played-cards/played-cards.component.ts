import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Card, PlayedCards} from '../../model/game.model';
import {CdkDragDrop} from '@angular/cdk/drag-drop';

@Component({
  selector: 'mc-played-cards',
  templateUrl: './played-cards.component.html',
  styleUrls: ['./played-cards.component.scss']
})
export class PlayedCardsComponent implements OnInit, OnChanges {

  @Input()
  public playedCards?: PlayedCards;

  @Input()
  public playerIds!: string[];

  cards: Card[][] = new Array(4);

  constructor() {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.cards = new Array(4).fill([]).map(() => new Array<Card>());
    if (this.playedCards?.cards !== undefined) {
      let playerIndex = this.playerIds.indexOf(this.playedCards.idOfStartingPlayer);
      this.playedCards.cards.forEach((c) => {
        this.cards[playerIndex].push(c);
        playerIndex = (playerIndex + 1) % this.playerIds.length;
      });
    }
  }

  cardDropped(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet');
  }
}
