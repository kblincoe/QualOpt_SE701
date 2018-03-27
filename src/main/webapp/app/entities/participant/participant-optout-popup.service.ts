import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Participant } from './participant.model';
import { Study } from '../study/study.model';
import { ParticipantService } from './participant.service';
import { StudyService } from '../study/study.service';

/**
 * This class defines methods to help with the functionality of the Participant OptOut Popup window. Essentially
 * it contains two methods, one that deals with opening the component, and the other that deals with the different
 * references that needs to be passed into the modal.
 */
@Injectable()
export class ParticipantOptOutPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private participantService: ParticipantService,
        private studyService: StudyService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, participantId?: number | any, studyId?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            // If neither IDs are null, find both and then reference both participant and study in the modalref
            if (participantId && studyId) {
                this.participantService.find(participantId).subscribe((participant) => {
                    this.studyService.find(studyId).subscribe((study) => {
                        this.ngbModalRef = this.participantOptOutModalRef(component, participant, study);
                        resolve(this.ngbModalRef);
                    })
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.participantOptOutModalRef(component, new Participant(), new Study());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    participantOptOutModalRef(component: Component, participant: Participant, study: Study): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});

        // Assigning the different instances to the modal
        modalRef.componentInstance.participant = participant;
        modalRef.componentInstance.study = study;

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
