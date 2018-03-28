import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EmailTemplate } from './emailTemplate.model';
import { ResponseWrapper, createRequestOption } from '../../../shared';

@Injectable()
export class EmailTemplateService {

    private resourceUrl = 'api/emailTemplates';

    constructor(private http: Http) { }

    create(emailTemplate: EmailTemplate): Observable<EmailTemplate> {
        const copy = this.convert(emailTemplate);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(emailTemplate: EmailTemplate): Observable<EmailTemplate> {
        const copy = this.convert(emailTemplate);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    
    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    get(userLogin: string): Observable<EmailTemplate[]> {
        return this.http.get(`${this.resourceUrl}/${userLogin}`).map((res: Response) => {
            return res.json();
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(emailTemplate: EmailTemplate): EmailTemplate {
        const copy: EmailTemplate = Object.assign({}, emailTemplate);
        return copy;
    }
}
