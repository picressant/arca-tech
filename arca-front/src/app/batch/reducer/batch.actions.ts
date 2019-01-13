import { Action } from '@ngrx/store';

export const START_BATCH    = '[BATCH] Start';
export const STOP_BATCH     = '[BATCH] Stop';
export const UPDATE_BATCH   = '[BATCH] Update';
export const INIT_BATCH     = '[BATCH] Init';

export class StartBatchAction implements Action {
    readonly type = START_BATCH;

    constructor() {}
}

export class StopBatchAction implements Action {
    readonly type = STOP_BATCH;

    constructor() {}
}

export class UpdateBatchAction implements Action {
    readonly type = UPDATE_BATCH;

    constructor(public payload) {}
}

export class InitBatchAction implements Action {
    readonly type = INIT_BATCH;

    constructor(public payload) {}
}


export type Actions = StartBatchAction | StopBatchAction | UpdateBatchAction | InitBatchAction ;
