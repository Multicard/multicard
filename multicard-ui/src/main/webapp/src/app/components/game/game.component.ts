import {Component, OnInit} from '@angular/core';
import {GameService} from '../../services/game.service';
import {Game, GameState} from '../../model/game.model';

@Component({
  selector: 'mc-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  gameState!: Game;

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.gameState = this.gameService.loadGameState();
  }

  isGameStateReadyToStart() {
    return this.gameState.state === GameState.readyToStart;
  }

  startGame() {
    this.gameService.startGame();
  }
}
