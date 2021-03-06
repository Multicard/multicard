import {Component, OnInit} from '@angular/core';
import {GameService} from '../../services/game.service';
import {Game, GameState} from '../../model/game.model';
import {Observable} from 'rxjs';

@Component({
  selector: 'mc-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  gameState$!: Observable<Game>;

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.gameState$ = this.gameService.loadGameState();
  }

  isGameStateReadyToStart(gameState: Game) {
    return gameState.state === GameState.readyToStart;
  }

  startGame() {
    this.gameService.startGame();
  }
}
