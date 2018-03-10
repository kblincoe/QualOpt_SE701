import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Rx';
import { GitHubAPIRequest } from './github-API-request.model';

/** Manages spawning up the GitHub User Search form as a modal and
 * sets the underlying model of the form to be a GitHubUserSearchAPIRequest object
 */
@Injectable()
export class GitHubPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
    ) {
        this.ngbModalRef = null;
    }

     /** Method used to open the Modal for the GitHub User Search form - returning
     * a Promise containing the NgbModalRef.
     */
    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            setTimeout(() => {
                this.ngbModalRef = this.githubModalRef(component, new GitHubAPIRequest());
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    /** Helper method to set the Modal - setting the model backing the form to
     * be a GitHubUserSearchAPIRequest. This model will then be modified and editable
     * by the user.
     */
    githubModalRef(component: Component, gitHubAPIRequest: GitHubAPIRequest): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.gitHubAPIRequest = gitHubAPIRequest;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
