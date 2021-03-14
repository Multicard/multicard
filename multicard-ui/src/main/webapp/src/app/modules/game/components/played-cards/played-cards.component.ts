import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CdkDragDrop} from '@angular/cdk/drag-drop';
import {CardDTO, PlayedCardDTO, PlayedCardsDTO} from '../../../../../app-gen/generated-model';
import {GameService} from '../../../../services/game.service';
import {FlyInAnimation} from '../card-pile/card-pile.component';

@Component({
  selector: 'mc-played-cards',
  templateUrl: './played-cards.component.html',
  styleUrls: ['./played-cards.component.scss']
})
export class PlayedCardsComponent implements OnInit, OnChanges {

  @Input() playedCards?: PlayedCardsDTO;
  @Input() playerIds!: string[];
  cards: PlayedCardDTO[][] = new Array(4);
  isLastCardPLayedByUser = false;
  haveAllPlayersPlayed = false;
  readonly flyInAnimationFromTop = FlyInAnimation.fromTop;
  readonly flyInAnimationFromLeft = FlyInAnimation.fromLeft;
  readonly flyInAnimationFromRight = FlyInAnimation.fromRight;

  constructor(
    private gameService: GameService) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.cards = new Array(4).fill([]).map(() => new Array<PlayedCardDTO>());
    if (this.playedCards?.cards !== undefined) {
      this.playedCards.cards.forEach((c) => {
        this.cards[this.playerIds.findIndex(pId => pId === c.playerId)].push(c);
      });
    }
    this.isLastCardPLayedByUser = this.gameService.isLastCardPLayedByUser(this.playedCards);
    this.haveAllPlayersPlayed = this.gameService.haveAllPlayersPlayed(this.playedCards);
  }

  getCardImage(card: CardDTO) {
    return 'assets/cards/' + (card.faceUp ? card.name : 'BLUE_BACK') + '.svg';
  }

  cardDroppedMiddle(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet', event);
    this.gameService.cardPlayed(event.item.data);
  }

  cardDroppedBottom(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet', event);
    this.gameService.cardPlayed(event.item.data);
  }

  isDragAndDropOfCardsAllowed() {
    return this.haveAllPlayersPlayed;
  }

  takePlayedCards() {
    if (this.isDragAndDropOfCardsAllowed() && this.playedCards?.cards) {
      this.gameService.tableCardsTakenByUser(this.playedCards.cards);
    }
  }

  redoLastCardAction() {

  }
}