import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Study } from './study.model';
import { StudyPopupService } from './study-popup.service';
import { StudyService } from './study.service';
import { Researcher, ResearcherService } from '../researcher';

@Component({
    selector: 'jhi-study-dialog',
    templateUrl: './study-dialog.component.html'
})
export class StudyDialogComponent implements OnInit {

    study: Study;
    authorities: any[];
    isSaving: boolean;

    researchers: Researcher[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private studyService: StudyService,
        private researcherService: ResearcherService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.researcherService.query().subscribe(
            (res: Response) => { this.researchers = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.study.id !== undefined) {
            this.studyService.update(this.study)
                .subscribe((res: Study) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.studyService.create(this.study)
                .subscribe((res: Study) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Study) {
        this.eventManager.broadcast({ name: 'studyListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackResearcherById(index: number, item: Researcher) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-study-popup',
    template: ''
})
export class StudyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private studyPopupService: StudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.studyPopupService
                    .open(StudyDialogComponent, params['id']);
            } else {
                this.modalRef = this.studyPopupService
                    .open(StudyDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
