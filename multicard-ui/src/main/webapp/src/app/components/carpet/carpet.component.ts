import {Component, Input, OnInit} from '@angular/core';
import {Stack} from '../../model/game.model';
import {CdkDragDrop} from '@angular/cdk/drag-drop';

@Component({
  selector: 'mc-carpet',
  templateUrl: './carpet.component.html',
  styleUrls: ['./carpet.component.scss']
})
export class CarpetComponent implements OnInit {

  @Input()
  public stacks!: Stack[];

  constructor() {
  }

  ngOnInit(): void {
  }

  cardDroppedBottom(event: CdkDragDrop<string[]>) {
    console.log('card dropped onto carpet in bottom area');
  }
}
