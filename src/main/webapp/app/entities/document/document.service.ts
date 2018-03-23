import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Document } from './document.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DocumentService {

    private resourceUrl = 'api/documents';

    constructor(private http: Http) { }

    downloadUrl(id: number): string {
        return `${this.resourceUrl}/${id}/download`
    }
}