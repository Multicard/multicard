import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {GameDTO, Gamestate, PlayerDTO, ScoreDTO} from '../../../../../app-gen/generated-model';
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

        const scoresWithSortedPlayers = game.scores?.filter(roundScore => roundScore?.playerScores?.length > 0)
          .map(roundScore => ({
            ...roundScore,
            playerScores: roundScore.playerScores.sort(
              (ps1, ps2) => this.getIndexOfSortedPlayer(ps1.playerId) - this.getIndexOfSortedPlayer(ps2.playerId))
          }));

        scoresWithSortedPlayers?.forEach(roundScore => {
          this.scores.push({
            id: '',
            round: roundScore.round,
            playerScores: [{playerId: '', score: roundScore.playerScores[0].score + roundScore.playerScores[2].score},
              {playerId: '', score: roundScore.playerScores[1].score + roundScore.playerScores[3].score}]
          });
        });

        this.total = scoresWithSortedPlayers?.reduce((sum, score) =>
            [sum[0] + score.playerScores[0].score + score.playerScores[2].score,
              sum[1] + score.playerScores[1].score + score.playerScores[3].score],
          [0, 0]);
      }));
  }

  hasScore() {
    return this.scores.length > 0;
  }

  getNoScoreText(game: GameDTO) {
    if (game.state !== Gamestate.GAME_ENDED) {
      return 'Zu diesem Spiel wurden noch keine Resultate erfasst';
    } else {
      return 'Das Spiel wurde ohne Resultate beendet';
    }
  }

  private getIndexOfSortedPlayer(playerId: string) {
    return this.sortedPLayers.findIndex(p => p.id === playerId);
  }
}
