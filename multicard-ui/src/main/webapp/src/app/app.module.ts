import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {TableComponent} from './components/table/table.component';
import {HandComponent} from './components/hand/hand.component';
import {GameComponent} from './components/game/game.component';
import {CarpetComponent} from './components/carpet/carpet.component';
import {StackComponent} from './components/stack/stack.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {PlayerComponent} from './components/player/player.component';

@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    HandComponent,
    GameComponent,
    CarpetComponent,
    StackComponent,
    PlayerComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
