import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import {Status, Study} from './study.model';
import { StudyService } from './study.service';

@Component({
    selector: 'jhi-study-detail',
    templateUrl: './study-detail.component.html'
})
export class StudyDetailComponent implements OnInit, OnDestroy {

    study: Study;
    status = Status;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private studyService: StudyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudies();
    }

    load(id) {
        this.studyService.find(id).subscribe((study) => {
            this.study = study;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    previousState() {
        history.back();
    }

    // Changes the status of the study to active.
    beginStudy() {
        this.study.status = Status.ACTIVE;
        this.updateStudy();
    }

    // Changes the status of the study to Completed.
    closeStudy() {
        this.study.status = Status.COMPLETED;
        this.updateStudy();
    }

    updateStudy(){
        this.studyService.update(this.study).subscribe();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studyListModification',
            (response) => this.load(this.study.id)
        );
    }
}
