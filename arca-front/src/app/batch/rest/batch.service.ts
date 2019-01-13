import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const URL = 'http://localhost:9991/rest';
const INIT_BATCH = URL + '/batch/init';
const START_BATCH = URL + '/batch/start';
const STOP_BATCH = URL + '/batch/stop';
const STATE_BATCH = URL + '/batch/state';
const VAL_CHART_BATCH = URL + '/batch/values/chart';
const VAL_TABLE_BATCH = URL + '/batch/values/table';

@Injectable({
    providedIn: 'root'
})
export class BatchService {

    constructor(private http: HttpClient) { }

    public initalizeBatch() {
        return this.http.get<number>(INIT_BATCH);
    }

    public startBatch() {
        return this.http.get(START_BATCH);
    }

    public stopBatch() {
        return this.http.get(STOP_BATCH);
    }

    public updateBatch() {
        return this.http.get<number>(STATE_BATCH);
    }

    public getChartsValue(iData: any) {
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'text/plain'
            })
        };
        return this.http.post<any[]>(VAL_CHART_BATCH, iData, httpOptions);
    }

    public getTableValue(iData: any) {
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'text/plain'
            })
        };
        return this.http.post<any[]>(VAL_TABLE_BATCH, iData, httpOptions);
    }

}
