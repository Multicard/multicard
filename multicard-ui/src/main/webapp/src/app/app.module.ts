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

@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    UserPlayerComponent,
    GameComponent,
    CarpetComponent,
    StackComponent,
    PlayerComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    MatButtonModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
