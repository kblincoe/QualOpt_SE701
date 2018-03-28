import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import {RequestOptions} from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Study, StudyInfo } from './study.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

import * as $ from "jquery";
import { Params } from '@angular/router';
import { Invitation } from '../participant/invitation-model';

@Injectable()
export class StudyService {

    private resourceUrl = 'api/studies';

    constructor(private http: Http) { }

    create(study: Study): Observable<Study> {
        const copy = this.convert(study);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(study: Study): Observable<Study> {
        const copy = this.convert(study);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Study> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    getStudyInfo(id: number): Observable<StudyInfo> {
        return this.http.get(`${this.resourceUrl}/${id}/info`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    send(study: Study): Observable<String[]> {
        let delay = this.getScheduledTimeAndDelay();
        // send an invitation
        let invitation = new Invitation();
        invitation.study = study;
        invitation.delay = delay;
        return this.http.post(`${this.resourceUrl}/send`, invitation)
            .map((res: Response) => {
                return res.json();
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(study: Study): Study {
        const copy: Study = Object.assign({}, study);
        return copy;
    }
    /*
     * Method is used to retrieve desired preset date-time and 
     * calculate the delay with which the email should be sent.
     */
    private getScheduledTimeAndDelay(): number{
        let delay = 0;
        // if user wants to send later:
        if ($('#sendLater').is(':checked')){
            let val = $('#setDate').val();
            if (val != null){
                delay = this.calculateDelay(val);
            } 
        }
        return delay;
    }

    private calculateDelay(delayString: string): number {
        // calculate delay by getting difference between two dates
        let diff = new Date(delayString).valueOf()  - new Date(StudyService.getCurrentDateTime()).valueOf();
        if (diff > 0){ 
            // diff is a time difference in milliseconds.
            return diff;
        }
        // return 0 if difference between two dates is negative. 
        return 0;
    }

    /*
     * Globally accessible method to getCurrentDateTime in specific format.
     */
    public static getCurrentDateTime(): string {
        let now = new Date();
        let month = (now.getMonth() + 1).toString();
        let day = now.getDate().toString() ;
        let hours = now.getHours().toString();
        let minutes = now.getMinutes().toString();
        let time = 'T' + + ':' + now.getMinutes().toString();
        if (+month < 10)
            month = "0" + month;
        if (+day < 10)
            day = "0" + day;
        if (+hours < 10)
            hours = "0" + hours;
        if (+minutes < 10)
            minutes = "0" + minutes;    
        let today = now.getFullYear() + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
        return today;
    }
}
