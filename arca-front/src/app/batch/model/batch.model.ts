import { ActionReducerMap } from '@ngrx/store';
import * as fromBatchReducer from '../reducer/batch.reducer';

export interface BatchState {
    status: BatchStateEnum;
    nbOfReadLines: number;
    nbOfLines: number;
}

export interface AppState {
  batch: BatchState;
}

// export interface BatchState {
//     batch: Batch;
// }

export enum BatchStateEnum {
    NON_INITIALIZED,
    INITIALISED,
   STARTED,
   STOPED,
   DONE
}
