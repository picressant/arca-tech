import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StartStopComponent } from './start-stop/start-stop.component';
import {ProgressBarModule} from 'angular-progress-bar';
import { StoreModule } from '@ngrx/store';
import * as BatchReducter from './reducer/batch.reducer';
import { HttpClientModule } from '@angular/common/http';
import { ChartComponent } from './chart/chart.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ProgressBarModule,
    HttpClientModule,
    StoreModule.forFeature('app_state', BatchReducter.reducers),
  ],
  declarations: [StartStopComponent, ChartComponent],
  exports: [StartStopComponent, ChartComponent]
})
export class BatchModule { }
