import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CardDTO, GameDTO, PlayerDTO} from '../../../../../app-gen/generated-model';
import {getCardImage} from '../../../../model/cardHelper';

@Component({
  selector: 'mc-uncovered-cards-dialog',
  templateUrl: './uncovered-cards-dialog.component.html',
  styleUrls: ['./uncovered-cards-dialog.component.scss']
})
export class UncoveredCardsDialogComponent implements OnInit {

  sortedPLayers!: PlayerDTO[];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: GameDTO,
    private dialogRef: MatDialogRef<UncoveredCardsDialogComponent>) {
  }

  ngOnInit(): void {
    this.sortedPLayers = this.data.players.sort((p1, p2) => p1.position - p2.position);
  }

  startNewRound() {
    this.dialogRef.close();
  }

  trackByPlayerId(index: number, player: PlayerDTO) {
    return player.id;
  }

  trackByCardId(index: number, card: CardDTO) {
    return card.id;
  }

  getCardImage(card: CardDTO) {
    return getCardImage(card);
  }
}
