import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response, Http } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { GitHubPopupService } from './participant-github-popup.service';
import { GitHubBackendCall } from './github-backend-call.service';
import { GitHubAPIRequest, UserType } from './github-API-request.model';

/** This class represents the component that controls the model and the
 * API call to the backend to send a GitHub API request to search users.
 * Note: The backend is responsible for the production of participants and the
 * success/fail HTTP Responses.
 */
@Component({
    selector: 'jhi-participant-github-query-dialog',
    templateUrl: './participant-github-query-dialog.component.html'
})
export class GitHubQueryDialogComponent implements OnInit {
    gitHubAPIRequest: GitHubAPIRequest

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private gitHubQueryPopupService: GitHubPopupService,
        private gitHubBackendCall: GitHubBackendCall
    ) {
    }

    ngOnInit() {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    /** Method called to submit the values the user entered into the GitHub User Search Form */
    sendAPIRequest() {
        console.log(this.gitHubAPIRequest);
        this.subscribeToAPIResponse(
            this.gitHubBackendCall.create(this.gitHubAPIRequest));

        // TODO: Show some kind of status/loading indication instead of just dismissing modal
        this.activeModal.dismiss(this.gitHubAPIRequest);
    }

    /** Subscribe method to handle result after the API call returns from the backend */
    private subscribeToAPIResponse(result: Observable<string>) {
        result.subscribe((res: string) =>
            this.onAPIResponseSuccess(res), (res: Response) => this.onAPIResponseError(res));
    }

    /** Method to handle the resulting string passed back in the body of a success (OK) HTTP Response */
    private onAPIResponseSuccess(result: string) {
        this.eventManager.broadcast({ name: 'participantListModification', content: 'OK'});
    }

    /** Method to handle the resulting string passed back in the body of a fail (bad request) HTTP Response */
    private onAPIResponseError(error) {
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.text(), null, null);
    }
}

@Component({
    selector: 'jhi-participant-github-query-popup',
    template: ''
})
export class GitHubQueryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private githubQueryPopupService: GitHubPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.githubQueryPopupService
                    .open(GitHubQueryDialogComponent as Component, params['id']);
            } else {
                this.githubQueryPopupService
                    .open(GitHubQueryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
