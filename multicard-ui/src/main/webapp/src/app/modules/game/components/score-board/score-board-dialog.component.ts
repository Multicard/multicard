import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {GameDTO, PlayerDTO, ScoreDTO} from '../../../../../app-gen/generated-model';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

@Component({
  selector: 'mc-score-board-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './score-board-dialog.component.html',
  styleUrls: ['./score-board-dialog.component.scss']
})
export class ScoreBoardDialogComponent implements OnInit {

  game$!: Observable<GameDTO>;
  sortedPLayers!: PlayerDTO[];
  scores: ScoreDTO[] = [];
  total: number[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) private data: Observable<GameDTO>) {
  }

  ngOnInit(): void {
    this.game$ = this.data
      .pipe(tap(game => {
        this.sortedPLayers = [...game.players].sort((p1, p2) => p1.position - p2.position);

        game.scores?.forEach(roundScore => {
          this.scores.push({
            id: '',
            round: roundScore.round,
            playerScores: [{playerId: '', score: roundScore.playerScores[0].score + roundScore.playerScores[2].score},
              {playerId: '', score: roundScore.playerScores[1].score + roundScore.playerScores[3].score}]
          });
        });

        this.total = game.scores?.reduce((sum, score) =>
            [sum[0] + score.playerScores[0].score + score.playerScores[2].score,
              sum[1] + score.playerScores[1].score + score.playerScores[3].score],
          [0, 0]);
      }));
  }

  hasScore() {
    return this.scores.length > 0;
  }
}
