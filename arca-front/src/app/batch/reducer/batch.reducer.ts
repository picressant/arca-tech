import * as BatchActions from './batch.actions';
import { createFeatureSelector, createSelector, ActionReducerMap } from '@ngrx/store';
import { BatchState, BatchStateEnum, AppState } from '../model/batch.model';

const initialState: BatchState = {
    status: BatchStateEnum.NON_INITIALIZED,
    nbOfReadLines: 0,
    nbOfLines: 0
};

export function reducer(state: BatchState = initialState, action: BatchActions.Actions): BatchState {
    let lState;

    switch (action.type) {
        case BatchActions.START_BATCH:
            return {
                ...state,
                status: BatchStateEnum.STARTED,
            };

        case BatchActions.STOP_BATCH:
            return {
                ...state,
                status: BatchStateEnum.STOPED,
            };

        case BatchActions.UPDATE_BATCH:
            lState = { ...state };
            lState.nbOfReadLines = action.payload.nbOfReadLines;
            if (lState.nbOfReadLines === lState.nbOfLines) {
                lState.status = BatchStateEnum.DONE;
            }
            return lState;

        case BatchActions.INIT_BATCH:
            lState = { ...state };
            lState.status = BatchStateEnum.INITIALISED;
            lState.nbOfLines = action.payload.nbOfLines;
            return lState;

        default:
            return state;
    }
}

// Reducers pour notre state
export const reducers: ActionReducerMap<AppState> = {
    batch: reducer
};


// globalState
export const getAppState = createFeatureSelector<AppState>('app_state');

// BatchState
export const getBatchState = createSelector(getAppState, (state: AppState) => {
    return state.batch;
});
