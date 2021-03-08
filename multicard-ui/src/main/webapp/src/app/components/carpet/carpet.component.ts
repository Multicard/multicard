import {Component, Input, OnInit} from '@angular/core';
import {PlayedCards, StackDTO} from '../../../app-gen/generated-model';

@Component({
  selector: 'mc-carpet',
  templateUrl: './carpet.component.html',
  styleUrls: ['./carpet.component.scss']
})
export class CarpetComponent implements OnInit {

  @Input()
  public stacks!: StackDTO[];

  @Input()
  public playedCards?: PlayedCards;

  @Input()
  public playerIds!: string[];

  constructor() {
  }

  ngOnInit(): void {
  }
}
