import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CardDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {createCardsForHand, getCardImage} from '../../../../model/cardHelper';
import {GameService} from '../../../../services/game.service';
import {CdkDragDrop} from '@angular/cdk/drag-drop';

@Component({
  selector: 'mc-user-player',
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit, OnChanges {

  @Input() player!: PlayerDTO;
  handCards: CardDTO[] = [];

  constructor(
    private gameService: GameService) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.handCards = createCardsForHand(this.player?.hand);
  }

  getCardImage(card: CardDTO) {
    return getCardImage(card);
  }

  handOutCard(card: CardDTO) {
    this.gameService.cardPlayed(card);
  }

  tableCardsDroppedOnStack(event: CdkDragDrop<string[]>) {
    console.log('table cards dropped onto user hand stack', event);
    this.gameService.tableCardsTakenByUser(event.item.data);
  }
}
