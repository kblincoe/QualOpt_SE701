import { Component, Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { GitHubAPIRequest } from './github-API-request.model';
import { Http, Response, RequestOptions } from '@angular/http';

/** Helper class to invoke the call to the back end resource handler (GitHubResource.java),
 * putting the GitHubUserSearchAPIRequest (containing the users desired search terms) into
 * the body of the HTTP Post request.
 */
@Injectable()
export class GitHubBackendCall {
    private resourceUrl = 'api/gitHubQuery';

    constructor(private http: Http) {
    }

    /** Creates and posts the HTTP request to the backend resource */
    create(gitHubAPIRequest: GitHubAPIRequest): Observable<string> {
        const copy = this.convert(gitHubAPIRequest);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.text();
        });
    }

    /** Private helper method to return a copy of the infomration, to ensure a const copy
     * is passed into the http.post() method
     */
    private convert(gitHubAPIRequest: GitHubAPIRequest): GitHubAPIRequest {
        const copy: GitHubAPIRequest = Object.assign({}, gitHubAPIRequest);
        return copy;
    }
}
