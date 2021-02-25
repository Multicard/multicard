import {Component, Input, OnInit} from '@angular/core';
import {GameConfiguration, GameState} from '../../model/game.model';

@Component({
  selector: 'mc-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  @Input()
  gameConfiguration!: GameConfiguration;

  @Input()
  gameState!: GameState;

  constructor() {
  }

  ngOnInit(): void {
  }

}
