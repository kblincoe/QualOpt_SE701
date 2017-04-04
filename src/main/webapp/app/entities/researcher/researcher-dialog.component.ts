import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Researcher } from './researcher.model';
import { ResearcherPopupService } from './researcher-popup.service';
import { ResearcherService } from './researcher.service';
import { Study, StudyService } from '../study';

@Component({
    selector: 'jhi-researcher-dialog',
    templateUrl: './researcher-dialog.component.html'
})
export class ResearcherDialogComponent implements OnInit {

    researcher: Researcher;
    authorities: any[];
    isSaving: boolean;

    studies: Study[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private researcherService: ResearcherService,
        private studyService: StudyService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.studyService.query().subscribe(
            (res: Response) => { this.studies = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.researcher.id !== undefined) {
            this.researcherService.update(this.researcher)
                .subscribe((res: Researcher) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.researcherService.create(this.researcher)
                .subscribe((res: Researcher) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: Researcher) {
        this.eventManager.broadcast({ name: 'researcherListModification', content: 'OK'});
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

    trackStudyById(index: number, item: Study) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-researcher-popup',
    template: ''
})
export class ResearcherPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private researcherPopupService: ResearcherPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.researcherPopupService
                    .open(ResearcherDialogComponent, params['id']);
            } else {
                this.modalRef = this.researcherPopupService
                    .open(ResearcherDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
