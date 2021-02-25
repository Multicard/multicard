import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {HandComponent} from './components/hand/hand.component';
import {GameComponent} from './components/game/game.component';

const routes: Routes = [
  {path: '', component: GameComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
