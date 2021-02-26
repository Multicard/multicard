import {Component, OnInit} from '@angular/core';
import {GameService} from '../../services/game.service';
import {GameState, GameConfiguration} from '../../model/game.model';

@Component({
  selector: 'mc-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
  gameConfiguration!: GameConfiguration;
  gameState!: GameState;

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    this.gameConfiguration = this.gameService.getGameConfiguration();
    this.gameState = this.gameService.getGameState();
  }

}
