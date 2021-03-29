import {ChangeDetectionStrategy, Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CardDTO, GameDTO, PlayerDTO, PlayerScoreDTO} from '../../../../../app-gen/generated-model';
import {getCardImage} from '../../../../model/cardHelper';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {GameService} from '../../../../services/game.service';

export class UncoveredCardsReturnType {
  initiateNewGame = false;
  showScore = false;

  constructor(initiateNewGame: boolean, showScore: boolean) {
    this.initiateNewGame = initiateNewGame;
    this.showScore = showScore;
  }
}

@Component({
  selector: 'mc-uncovered-cards-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './uncovered-cards-dialog.component.html',
  styleUrls: ['./uncovered-cards-dialog.component.scss']
})
export class UncoveredCardsDialogComponent implements OnInit {

  game$!: Observable<GameDTO>;
  sortedPLayers!: PlayerDTO[];
  playersScore!: Map<string, number | undefined>;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Observable<GameDTO>,
    private dialogRef: MatDialogRef<UncoveredCardsDialogComponent>,
    private gameService: GameService) {
  }

  ngOnInit(): void {
    this.game$ = this.data
      .pipe(tap(game => {
        this.sortedPLayers = [...game.players].sort((p1, p2) => p1.position - p2.position);
        this.playersScore = this.convertPlayersScore(game);
      }));
  }

  submitScore(game: GameDTO) {
    const playerScores: PlayerScoreDTO[] = [];
    this.playersScore.forEach((score, playerId) => playerScores.push({playerId, score: score ? score : 0}));
    const scoreIdCurrentRound = game.scores?.find(s => s.round === game.currentRound)?.id;
    this.gameService.setScore({id: scoreIdCurrentRound ? scoreIdCurrentRound : '', round: game.currentRound, playerScores});
  }

  startNewRound() {
    this.dialogRef.close(new UncoveredCardsReturnType(true,  false));
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

  isScoreRegistered(game: GameDTO) {
    return !!game?.scores?.find(s => s.round === game.currentRound);
  }

  private convertPlayersScore(game: GameDTO) {
    const playersScore = new Map();
    const registeredScore = game?.scores?.find(s => s.round === game.currentRound);
    if (registeredScore) {
      registeredScore.playerScores.forEach(ps => playersScore.set(ps.playerId, ps.score));
    } else {
      game.players.forEach((p) => playersScore.set(p.id, p.stacks[0]?.cards?.length > 0 ? undefined : 0));
    }
    return playersScore;
  }
}
