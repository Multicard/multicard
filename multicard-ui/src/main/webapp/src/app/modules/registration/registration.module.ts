import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RegistrationComponent} from './components/registration/registration.component';
import {RegistrationRoutingModule} from './registration-routing.module';
import {FormsModule} from '@angular/forms';
import {MatSelectModule} from '@angular/material/select';


@NgModule({
  declarations: [RegistrationComponent],
  imports: [
    CommonModule,
    RegistrationRoutingModule,
    FormsModule,
    MatSelectModule
  ]
})
export class RegistrationModule {
}
