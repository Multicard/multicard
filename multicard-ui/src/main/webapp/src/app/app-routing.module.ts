import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'registration' },
  { path: 'registration', loadChildren: () => import('./modules/registration/registration.module').then(m => m.RegistrationModule) },
  { path: 'game', loadChildren: () => import('./modules/game/game.module').then(m => m.GameModule) },
  { path: '**', redirectTo: 'registration' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
