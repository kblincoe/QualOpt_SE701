import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Study } from './study.model';
import { StudyPopupService } from './study-popup.service';
import { StudyService } from './study.service';

@Component({
    selector: 'jhi-study-delete-dialog',
    templateUrl: './study-delete-dialog.component.html'
})
export class StudyDeleteDialogComponent {

    study: Study;

    constructor(
        private studyService: StudyService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.studyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'studyListModification',
                content: 'Deleted an study'
            });
            this.activeModal.dismiss(true);
            window.history.back();
        });
    }
}

@Component({
    selector: 'jhi-study-delete-popup',
    template: ''
})
export class StudyDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studyPopupService: StudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.studyPopupService
                .open(StudyDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
