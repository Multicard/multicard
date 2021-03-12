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
import {HttpClientModule} from '@angular/common/http';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {GameRoutingModule} from './game-routing.module';


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
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    DragDropModule,
    GameRoutingModule
  ]
})
export class GameModule { }
