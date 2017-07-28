import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Researcher } from './researcher.model';
import { ResearcherPopupService } from './researcher-popup.service';
import { ResearcherService } from './researcher.service';
import { Study, StudyService } from '../study';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-researcher-dialog',
    templateUrl: './researcher-dialog.component.html'
})
export class ResearcherDialogComponent implements OnInit {

    researcher: Researcher;
    isSaving: boolean;

    studies: Study[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private researcherService: ResearcherService,
        private studyService: StudyService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.studyService.query()
            .subscribe((res: ResponseWrapper) => { this.studies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.researcher.id !== undefined) {
            this.subscribeToSaveResponse(
                this.researcherService.update(this.researcher));
        } else {
            this.subscribeToSaveResponse(
                this.researcherService.create(this.researcher));
        }
    }

    private subscribeToSaveResponse(result: Observable<Researcher>) {
        result.subscribe((res: Researcher) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Researcher) {
        this.eventManager.broadcast({ name: 'researcherListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
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

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private researcherPopupService: ResearcherPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.researcherPopupService
                    .open(ResearcherDialogComponent as Component, params['id']);
            } else {
                this.researcherPopupService
                    .open(ResearcherDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
