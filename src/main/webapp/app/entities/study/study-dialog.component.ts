import { Component, OnInit, OnDestroy, HostListener, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';
import { NgForm } from '@angular/forms';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Study } from './study.model';
import { StudyPopupService } from './study-popup.service';
import { StudyService } from './study.service';
import { User, UserService } from '../../shared';
import { Participant, ParticipantService } from '../participant';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-study-dialog',
    templateUrl: './study-dialog.component.html'
})
export class StudyDialogComponent implements OnInit {

    study: Study;
    isSaving: boolean;

    users: User[];

    participants: Participant[];

    @ViewChild('editForm') editForm: NgForm;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private studyService: StudyService,
        private userService: UserService,
        private participantService: ParticipantService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.participantService.query()
            .subscribe((res: ResponseWrapper) => { this.participants = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, study, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                study[field] = base64Data;
                study[`${field}ContentType`] = file.type;
            });
        }
    }

    /*
    * This method is called when the user tries to cancel out of the study form.
    * If the user has unsaved changes there are ask whether they want to save the
    * to the save the changes before exiting.
    */
    clear() {
        if (this.hasUnsavedChanges()) {
            this.activeModal.dismiss('cancel');
        } else {
            if (confirm('Changes you made have not been saved! Would you like the discard the changes?')) {
                this.activeModal.dismiss('cancel');
            }
        }
    }

    /*
    * This method listens for events such as the window being closed or refreshed.
    * If the user has unsaved changes a message is displayed warning the user of
    * the unsaved changes.
    */
    @HostListener('window:beforeunload', ['$event'])
    unloadNotification($event: any) {
        if (!this.hasUnsavedChanges()) {
            $event.returnValue = true;
        }
    }

    private hasUnsavedChanges(): boolean {
      return this.editForm.submitted || !this.editForm.dirty;
    }

    save() {
        this.isSaving = true;
        if (this.study.id !== undefined) {
            this.subscribeToSaveResponse(
                this.studyService.update(this.study));
        } else {
            this.subscribeToSaveResponse(
                this.studyService.create(this.study));
        }
    }

    changeTab(tab) {
        document.getElementById(tab).click();
        console.log(document.getElementById(tab));
    }

    private subscribeToSaveResponse(result: Observable<Study>) {
        result.subscribe((res: Study) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Study) {
        this.eventManager.broadcast({ name: 'studyListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackParticipantById(index: number, item: Participant) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    getTitle() {
        if ( this.study.id != null ) {
            return 'Edit Study';
        }else {
            return 'Create Study';
        }
    }
}

@Component({
    selector: 'jhi-study-popup',
    template: ''
})
export class StudyPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studyPopupService: StudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                if (this.route.snapshot.data.copy) {
                    this.studyPopupService
                    .copy(StudyDialogComponent as Component, params['id'])
                } else {
                    this.studyPopupService
                    .open(StudyDialogComponent as Component, params['id']);
                }
            } else {
                this.studyPopupService
                    .open(StudyDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
