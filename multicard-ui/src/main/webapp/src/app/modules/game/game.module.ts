import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TableComponent} from './components/table/table.component';
import {UserPlayerComponent} from './components/user-player/user-player.component';
import {CarpetComponent} from './components/carpet/carpet.component';
import {StackComponent} from './components/stack/stack.component';
import {PlayerComponent} from './components/player/player.component';
import {PlayedCardsComponent} from './components/played-cards/played-cards.component';
import {CardPileComponent} from './components/card-pile/card-pile.component';
import {GameComponent} from './components/game/game.component';
import {GameRoutingModule} from './game-routing.module';
import {SharedModule} from '../shared/shared.module';


@NgModule({
  declarations: [
    TableComponent,
    UserPlayerComponent,
    GameComponent,
    CarpetComponent,
    StackComponent,
    PlayerComponent,
    PlayedCardsComponent,
    CardPileComponent
  ],
  imports: [
    CommonModule,
    GameRoutingModule,
    SharedModule
  ]
})
export class GameModule { }
