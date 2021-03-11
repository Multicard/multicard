import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CdkDragDrop} from '@angular/cdk/drag-drop';
import {PlayedCardDTO, PlayedCardsDTO} from '../../../app-gen/generated-model';
import {GameService} from '../../services/game.service';

@Component({
  selector: 'mc-played-cards',
  templateUrl: './played-cards.component.html',
  styleUrls: ['./played-cards.component.scss']
})
export class PlayedCardsComponent implements OnInit, OnChanges {

  @Input()
  public playedCards?: PlayedCardsDTO;

  @Input()
  public playerIds!: string[];

  cards: PlayedCardDTO[][] = new Array(4);

  constructor(
    private gameService: GameService
  ) {}

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.cards = new Array(4).fill([]).map(() => new Array<PlayedCardDTO>());
    if (this.playedCards?.cards !== undefined) {
      this.playedCards.cards.forEach((c) => {
        this.cards[this.playerIds.findIndex(pId => pId === c.playerId)].push(c);
      });
    }
  }

  cardDroppedMiddle(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet', event);
    this.gameService.cardPlayed(event.item.data);
  }

  cardDroppedBottom(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet', event);
    this.gameService.cardPlayed(event.item.data);
  }
}
