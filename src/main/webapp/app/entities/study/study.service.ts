import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Study, StudyInfo } from './study.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

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
        return this.http.post(`${this.resourceUrl}/send`, study)
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
}
