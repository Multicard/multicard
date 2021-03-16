import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {GameService} from '../../../../services/game.service';
import {Observable} from 'rxjs';
import {GameDTO, Gamestate} from '../../../../../app-gen/generated-model';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'mc-game',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  gameState$!: Observable<GameDTO>;

  constructor(
    private route: ActivatedRoute,
    private gameService: GameService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(p => {
      const gameId = p.get('gameId');
      const playerId = p.get('playerId');
      if (gameId !== null && playerId !== null) {
        this.gameState$ = this.gameService.initGame(gameId, playerId);
      } else {
        console.error('gameId or playerId is not set', this.route);
      }
    });
  }

  isGameStarteable(gameState: GameDTO) {
    return gameState.state === Gamestate.READYTOSTART && gameState.players[0]?.organizer;
  }

  startGame() {
    this.gameService.startGame();
  }
}
