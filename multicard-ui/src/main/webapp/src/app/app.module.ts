import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {TableComponent} from './components/table/table.component';
import {GameComponent} from './components/game/game.component';
import {CarpetComponent} from './components/carpet/carpet.component';
import {StackComponent} from './components/stack/stack.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {PlayerComponent} from './components/player/player.component';
import {UserPlayerComponent} from './components/user-player/user-player.component';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {rxStompConfig} from './rx-stomp.config';
import {DragDropModule} from '@angular/cdk/drag-drop';
import { PlayedCardsComponent } from './components/played-cards/played-cards.component';
import { CardPileComponent } from './components/card-pile/card-pile.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
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
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    DragDropModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: rxStompConfig,
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig],
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
