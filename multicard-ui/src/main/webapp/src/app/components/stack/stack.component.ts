import {Component, OnInit} from '@angular/core';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'mc-stack',
  templateUrl: './stack.component.html',
  styleUrls: ['./stack.component.scss'],
  animations: [
    trigger('flyOut', [
      transition(':leave', [
        animate(400, style({transform: '{{translateExpression}}'})),
      ])
    ])
  ]
})
export class StackComponent implements OnInit {

  public cards = new Array(20).fill({}).map(() => ({}));

  constructor() {
  }

  ngOnInit(): void {
  }

  public get3DMargin(i: number): number {
    return 2 * Math.round(i / 4);
  }

  public onStackClick(): void {
    if (this.cards.length > 0) {
      this.cards.pop();
    }
  }

  public getFlyOutTranslateExpression(i: number) {
    // TODO remove dummy code
    switch (i % 4) {
      case 0:
        return 'translatey(50vh) rotate(180deg)';
      case 1:
        return 'translatex(-50vh) rotate(90deg)';
      case 2:
        return 'translatey(-30vh) rotate(180deg)';
      case 3:
      default:
        return 'translatex(50vh) rotate(90deg)';
    }
  }
}
