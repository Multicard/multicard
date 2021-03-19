import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RegistrationComponent} from './components/registration/registration.component';
import {ConfigurationComponent} from './components/configuration/configuration.component';


const registrationRoutes: Routes = [
    { path: '', pathMatch: 'full', component: RegistrationComponent},
    { path: 'config', component: ConfigurationComponent},
];

@NgModule({
  imports: [RouterModule.forChild(registrationRoutes)],
  exports: [RouterModule]
})
export class RegistrationRoutingModule { }
