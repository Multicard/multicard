import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {GameDTO, ScoreDTO} from '../../../../../app-gen/generated-model';

@Component({
  selector: 'mc-score-board-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './score-board-dialog.component.html',
  styleUrls: ['./score-board-dialog.component.scss']
})
export class ScoreBoardDialogComponent implements OnInit {

  game: GameDTO;
  scores: ScoreDTO[] = [];
  total: number[] = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: GameDTO,
  ) {
    this.game = data;
  }

  ngOnInit(): void {
    this.game.scores?.forEach(roundScore => {
      this.scores.push({
        id: '',
        round: roundScore.round,
        playerScores: [{playerId: '', score: roundScore.playerScores[0].score + roundScore.playerScores[2].score},
          {playerId: '', score: roundScore.playerScores[1].score + roundScore.playerScores[3].score}]
      });
    });
  }

  hasScore() {
    return this.game.scores?.length > 0;
  }
}
