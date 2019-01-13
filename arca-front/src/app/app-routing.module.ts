import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StartStopComponent } from './batch/start-stop/start-stop.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ChartComponent } from './batch/chart/chart.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: WelcomeComponent },
  { path: 'startAndStop', component: StartStopComponent },
  { path: 'chart', component: ChartComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
