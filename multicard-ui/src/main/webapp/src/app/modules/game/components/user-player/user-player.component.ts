import {ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CardDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {createCardsForHand, getCardImage} from '../../../../model/cardHelper';
import {GameService} from '../../../../services/game.service';
import {CdkDragDrop} from '@angular/cdk/drag-drop';
import {Observable} from 'rxjs';

@Component({
  selector: 'mc-user-player',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './user-player.component.html',
  styleUrls: ['./user-player.component.scss']
})
export class UserPlayerComponent implements OnInit, OnChanges {

  @Input() player!: PlayerDTO;
  handCards: CardDTO[] = [];
  isRevertStackTakenAllowed = false;
  tableStackDragAndDropInProgress$!: Observable<boolean>;

  constructor(
    private gameService: GameService) {
  }

  ngOnInit(): void {
    this.tableStackDragAndDropInProgress$ = this.gameService.getTableStackDragAndDropInProgressObservable();
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.handCards = createCardsForHand(this.player?.hand);
    this.isRevertStackTakenAllowed = this.gameService.isRevertStackTakenAllowed();
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

  trackByCardId(index: number, card: CardDTO) {
    return card.id;
  }

  redoStackTakenAction() {
    this.gameService.revertLastAction();
  }

  onDragStarted() {
    this.gameService.setPlayerCardDragAndDropOntoTableInProgress(true);
  }

  onDragEnded() {
    this.gameService.setPlayerCardDragAndDropOntoTableInProgress(false);
  }
}
