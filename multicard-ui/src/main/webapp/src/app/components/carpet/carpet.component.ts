import {Component, Input, OnInit} from '@angular/core';
import {PlayedCards, Stack} from '../../model/game.model';

@Component({
  selector: 'mc-carpet',
  templateUrl: './carpet.component.html',
  styleUrls: ['./carpet.component.scss']
})
export class CarpetComponent implements OnInit {

  @Input()
  public stacks!: Stack[];

  @Input()
  public playedCards?: PlayedCards;

  @Input()
  public playerIds!: string[];

  constructor() {
  }

  ngOnInit(): void {
  }
}
