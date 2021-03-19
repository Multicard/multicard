import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegistrationComponent} from './components/registration/registration.component';
import {RegistrationRoutingModule} from './registration-routing.module';
import {PlayerRegistrationComponent} from './components/player-registration/player-registration.component';
import {SharedModule} from '../shared/shared.module';
import {ConfigurationComponent} from './components/configuration/configuration.component';


@NgModule({
  declarations: [
    RegistrationComponent,
    PlayerRegistrationComponent,
    ConfigurationComponent
  ],
  imports: [
    CommonModule,
    RegistrationRoutingModule,
    SharedModule
  ],
  entryComponents: [
    PlayerRegistrationComponent
  ]
})
export class RegistrationModule {
}
