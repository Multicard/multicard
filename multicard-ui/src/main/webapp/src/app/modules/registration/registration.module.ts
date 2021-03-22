import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegistrationComponent} from './components/registration/registration.component';
import {RegistrationRoutingModule} from './registration-routing.module';
import {PlayerRegistrationComponent} from './components/player-registration/player-registration.component';
import {SharedModule} from '../shared/shared.module';
import {ConfigurationComponent} from './components/configuration/configuration.component';
import {PlayerRegistrationDialogComponent} from './components/player-registration-dialog/player-registration-dialog.component';
import {QRCodeModule} from 'angularx-qrcode';


@NgModule({
  declarations: [
    RegistrationComponent,
    PlayerRegistrationDialogComponent,
    PlayerRegistrationComponent,
    ConfigurationComponent
  ],
  imports: [
    CommonModule,
    QRCodeModule,
    RegistrationRoutingModule,
    SharedModule
  ],
  entryComponents: [
    PlayerRegistrationDialogComponent
  ]
})
export class RegistrationModule {
}
