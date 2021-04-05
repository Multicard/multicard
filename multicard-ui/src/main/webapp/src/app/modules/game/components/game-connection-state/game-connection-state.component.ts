import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {GameService} from '../../../../services/game.service';

@Component({
  selector: 'mc-game-connection-state',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './game-connection-state.component.html',
  styleUrls: ['./game-connection-state.component.scss']
})
export class GameConnectionStateComponent implements OnInit {
  connectionActive$!: Observable<boolean>;

  constructor(
    private gameService: GameService) {
  }

  ngOnInit(): void {
    this.connectionActive$ = this.gameService.connectionActive$;
  }
}
