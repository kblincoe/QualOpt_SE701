import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

import { Study } from './study.model';
import { StudyPopupService } from './study-popup.service';
import { StudyService } from './study.service';

@Component({
    selector: 'jhi-study-confirm-dialog',
    templateUrl: './study-confirm-invitation-ToNew.component.html'
})
export class StudyConfirmToNewDialogComponent {

    study: Study;

    constructor(
        private studyService: StudyService,
        public activeModal: NgbActiveModal) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

     confirmSendToNew() {
	console.log('Sending invitation email ...');
        this.studyService.sendToNew(this.study).subscribe();
        this.activeModal.dismiss('sent');
    }

}

@Component({
    selector: 'jhi-study-confirm-popup',
    template: ''
})
export class StudyConfirmToNewPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studyPopupService: StudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.studyPopupService
                .open(StudyConfirmToNewDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
