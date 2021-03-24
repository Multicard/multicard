import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'mc-player-name',
  templateUrl: './player-name.component.html',
  styleUrls: ['./player-name.component.scss']
})
export class PlayerNameComponent implements OnInit {

  @Input() playerName?: string;
  @Input() showState = true;
  @Input() isAlive = false;

  constructor() { }

  ngOnInit(): void {
  }
}
