import {Component, OnInit} from '@angular/core';
import {GameService} from '../../services/game.service';
import {Observable} from 'rxjs';
import {GameDTO, Gamestate} from '../../../app-gen/generated-model';

@Component({
  selector: 'mc-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  gameState$!: Observable<GameDTO>;

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.gameState$ = this.gameService.initGame();
  }

  isGameStarteable(gameState: GameDTO) {
    return gameState.state === Gamestate.READYTOSTART && gameState.players[0]?.organizer;
  }

  startGame() {
    this.gameService.startGame();
  }
}
