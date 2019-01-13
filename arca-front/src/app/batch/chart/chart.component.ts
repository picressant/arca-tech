import { Component, OnInit } from '@angular/core';
import * as CanvasJS from '../../../assets/js/canvasjs.min';
import { BatchService } from '../rest/batch.service';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-chart',
    templateUrl: './chart.component.html',
    styleUrls: ['./chart.component.less']
})
export class ChartComponent implements OnInit {

    values: any[];

    chart: CanvasJS.chart;
    tableValues: any[];

    granularity = 1;
    chosenYear = 2008;
    startDate: Date;
    endDate: Date;

    secondChartSource = '';

    constructor(private batchRest: BatchService) { }

    private _buildFilter(isSecondChart: boolean = false) {
        return {
            year: this.chosenYear,
            granularity: this.granularity,
            startingDate: (this.startDate) ? this.startDate : null,
            endingDate: (this.endDate) ? this.endDate : null,
            source: (isSecondChart) ? this.secondChartSource : ''
        }
    }

    ngOnInit() {
        this.batchRest.getChartsValue(this._buildFilter()).pipe(
            map((data: any[]) => {
                return data.map(iVal => ({ label: iVal.TimeStp, y: iVal.Value }));
            })).subscribe((iPoints) => {
                const lChartPointsAll = {
                    name: 'All',
                    type: 'spline',
                    showInLegend: true,
                    dataPoints: iPoints
                };

                this.chart = new CanvasJS.Chart('chartContainer', {
                    animationEnabled: true,
                    title: {
                        text: 'Values by Time'
                    },
                    axisX: {
                        title: 'Time'
                    },
                    axisY: {
                        title: 'Values',
                        includeZero: false
                    },
                    legend: {
                        cursor: 'pointer',
                        fontSize: 16
                    },
                    toolTip: {
                        shared: true
                    },
                    data: [lChartPointsAll]
                });
                this.chart.render();
            });

        this.batchRest.getTableValue(this._buildFilter()).subscribe( (iValues: any[]) => {
            this.tableValues = iValues;
        });
    }

    refreshChart() {
        this.batchRest.getChartsValue(this._buildFilter()).pipe(
            map((data: any[]) => {
                return data.map(iVal => ({ label: iVal.TimeStp, y: iVal.Value }));
            })).subscribe((iPoints) => {
                const lChartPointsAll = {
                    name: 'All',
                    type: 'spline',
                    showInLegend: true,
                    dataPoints: iPoints
                };
                this.chart.options.data[0] = lChartPointsAll;
                this.chart.render();
            });
        if (this.secondChartSource !== '') {
           this.batchRest.getChartsValue(this._buildFilter(true)).pipe(
            map((data: any[]) => {
                return data.map(iVal => ({ label: iVal.TimeStp, y: iVal.Value }));
            })).subscribe((iPoints) => {
                const lChartPoints = {
                    name: this.secondChartSource,
                    type: 'spline',
                    showInLegend: true,
                    dataPoints: iPoints
                };
                this.chart.options.data[1] = lChartPoints;
                this.chart.render();
            });
        }
    }

    refreshTable() {
        this.batchRest.getTableValue(this._buildFilter()).subscribe( (iValues: any[]) => {
            this.tableValues = iValues;
        });
    }
}
