import {Component, Input, OnInit} from '@angular/core';
import {PlayerDTO} from '../../../../../app-gen/generated-model';

@Component({
  selector: 'mc-player-name',
  templateUrl: './player-name.component.html',
  styleUrls: ['./player-name.component.scss']
})
export class PlayerNameComponent implements OnInit {

  @Input() playerName?: string;

  constructor() { }

  ngOnInit(): void {
  }

}
