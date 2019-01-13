import { Component, OnInit, OnDestroy } from '@angular/core';
import { BatchService } from '../rest/batch.service';
import { Store } from '@ngrx/store';
import { BatchState, BatchStateEnum, AppState } from '../model/batch.model';
import { InitBatchAction, StartBatchAction, StopBatchAction, UpdateBatchAction } from '../reducer/batch.actions';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';

import * as fromBatchReducer from '../reducer/batch.reducer';

@Component({
    selector: 'app-start-stop',
    templateUrl: './start-stop.component.html',
    styleUrls: ['./start-stop.component.less']
})
export class StartStopComponent implements OnInit, OnDestroy {

    currentPercent: number;
    displayedTextProgress: string;

    public batch$: Observable<BatchState>;
    subscriptionBatch: any;

    isInitialized = false;

    private _innerTimeu;
    constructor(
        private batchRest: BatchService,
        private store: Store<AppState>) {

        this.displayedTextProgress = 'NON_INITIALIZED';
    }

    ngOnInit() {
        this.batch$ = this.store.select(fromBatchReducer.getBatchState);

        this.batch$.pipe(take(1)).subscribe((batch: BatchState) => {
            this.currentPercent = batch.nbOfReadLines / batch.nbOfLines;
            this.displayedTextProgress = batch.nbOfReadLines + ' / ' + batch.nbOfLines;
            this.isInitialized = (batch.status !== BatchStateEnum.NON_INITIALIZED);
            if (this.isInitialized) {
                this.setIntervalUpdate();
            }
        });

        this.subscriptionBatch = this.batch$.subscribe((batch: BatchState) => {
            this.currentPercent = batch.nbOfReadLines / batch.nbOfLines;
            this.displayedTextProgress = batch.nbOfReadLines + ' / ' + batch.nbOfLines;
            this.isInitialized = (batch.status !== BatchStateEnum.NON_INITIALIZED);
        });
    }

    ngOnDestroy() {
        this.subscriptionBatch.unsubscribe();
        clearInterval(this._innerTimeu);
    }

    private setIntervalUpdate() {
        this.batchRest.updateBatch()
            .subscribe((ival: number) => this.store.dispatch(new UpdateBatchAction({ nbOfReadLines: ival })));
        this._innerTimeu = setInterval(() => {
            this.batchRest.updateBatch()
                .subscribe((ival: number) => this.store.dispatch(new UpdateBatchAction({ nbOfReadLines: ival })));
        }, 5000);
    }

    public startBatch() {
        alert('Starting');
        if (!this.isInitialized) {
            this.batchRest.initalizeBatch().subscribe(iVal => {
                this.store.dispatch(new InitBatchAction({ nbOfLines: iVal }));
                this._startImpl();
            });
        } else {
            this._startImpl();
        }
    }

    private _startImpl() {
        this.batchRest.startBatch().subscribe(() => {
            this.store.dispatch(new StartBatchAction());
            this.setIntervalUpdate();
        });
    }


    public stopBatch() {
        alert('Stopping');
        this.batchRest.stopBatch().subscribe(() => {
            this.store.dispatch(new StopBatchAction());
            clearInterval(this._innerTimeu);
        });
    }
}
