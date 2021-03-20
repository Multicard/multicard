import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'mc-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.scss']
})
export class ConfigurationComponent implements OnInit {

  gameForm = this.fb.group({
    gameName: ['', Validators.required],
    numberOfPlayers: [{value: 4, disabled: true}],
    gameType: [{value: 'Schieber', disabled: true}],
    numberOfGivenCardsPerPlayer: [{value: 9, disabled: true}]
  });

  gameTypes: string[] = ['Schieber', 'Tschou Sepp', 'frei konfigurierbar'];

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
  }

  onSubmit() {
    console.log('submit');
  }
}
