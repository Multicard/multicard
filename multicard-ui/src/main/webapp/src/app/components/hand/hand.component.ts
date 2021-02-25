import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'mc-hand',
  templateUrl: './hand.component.html',
  styleUrls: ['./hand.component.scss']
})
export class HandComponent implements OnInit {

  public cards = ['AS', '3C', '10H', 'JC', '7D', 'QD', 'KS', 'AH', '5C', '9D'];

  constructor() {
  }

  ngOnInit(): void {
  }

}
