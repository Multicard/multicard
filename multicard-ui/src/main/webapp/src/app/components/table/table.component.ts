import {Component, Input, OnInit} from '@angular/core';
import {Game} from '../../model/game.model';

@Component({
  selector: 'mc-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

  @Input()
  gameState!: Game;

  constructor() {
  }

  ngOnInit(): void {
  }

}
