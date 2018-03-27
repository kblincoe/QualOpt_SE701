import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

import { Study } from './study.model';
import { StudyPopupService } from './study-popup.service';
import { StudyService } from './study.service';

import * as _swal from 'sweetalert';
import { SweetAlert } from 'sweetalert/typings/core';
import {StudyDetailComponent} from "./study-detail.component";
import {JhiEventManager} from "ng-jhipster";
const swal: SweetAlert = _swal as any;

@Component({
    selector: 'jhi-study-confirm-dialog',
    templateUrl: './study-confirm-invitation-ToNew.component.html'
})
export class StudyConfirmToNewDialogComponent {

    study: Study;

    constructor(
        private studyService: StudyService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmSendToNew() {
        console.log('Sending invitation email ...');
        this.studyService.sendToNew(this.study).subscribe(bouncedMail => {
            console.log(`Bounced: ${bouncedMail}`);
            this.study.bouncedMail = bouncedMail.join(', ');
            this.studyService.update(this.study).subscribe(() =>{
                this.eventManager.broadcast({ name: 'studyListModification', content: 'OK' });
		this.studyService.addToInvitedEmail(this.study).subscribe()});
            if (bouncedMail.length > 0){
                this.showBouncedMailAlert(bouncedMail);
            }
        });
        this.activeModal.dismiss('sent');
    }

    private showBouncedMailAlert(bouncedMail: String[]){
        let studyName = this.study.name;
        let studyUrl = location.origin + "/#/study/" + this.study.id;
        let formattedMail = bouncedMail.join('\n');
        swal({
            title: `Failed to send invitations for study: ${studyName}`,
            text: `The following email addresses bounced:\n ${formattedMail}`,
            icon: 'error',
            closeOnClickOutside: false,
            buttons: {
                confirm: { text: `View ${studyName}` }
            }
        }).then(viewStudy => {
            if (viewStudy){
                location.href = studyUrl;
            }
        });
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
