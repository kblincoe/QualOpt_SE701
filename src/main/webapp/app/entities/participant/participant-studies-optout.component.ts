import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ParticipantOptOutPopupService } from './participant-optout-popup.service';
import { Participant } from './participant.model';
import { ParticipantService } from './participant.service';
import { Study } from '../study/study.model';
import { StudyService } from '../study/study.service';
import { Response } from '_debugger';

@Component({
    selector: 'jhi-participant-studies-optout',
    templateUrl: './participant-studies-optout.component.html'
})
export class ParticipantStudiesOptOutComponent {

    participant: Participant;
    study: Study;

    constructor(
        private route: ActivatedRoute,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private participantService: ParticipantService,
        private studyService: StudyService
    ) {
    }

    /**
     * This method deals with the logic of when the optout button is pressed. Essentially, it calls a
     * helper method to delete the relationship between the study and the participant and then broadcasts
     * this to the event manager, to make sure that the listeners know that this has happened.
     */
    confirmOptOut() {
        this.participantService.deleteStudy(this.participant.email, this.study.id).subscribe((res: Participant) => {
            this.eventManager.broadcast({
                name: 'participantStudiesListModification',
                content: 'Opted Out of a Study'
            });
        });
        this.activeModal.dismiss(true);
    }

    /**
     * This method, when called, closes the current popup.
     */
    clear() {
        this.activeModal.dismiss('cancel');
    }
}

@Component({
    selector: 'jhi-participant-studies-optout-popup',
    template: ''
})
export class ParticipantStudiesOptOutPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private participantOptOutPopupService: ParticipantOptOutPopupService
    ) {}

    /**
     * This is the initaliser method for the component. Essentially, it calls the service to open the
     * ParticipanStudiesOptOutComponent as a popup with the desired parameters.
     */
    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.participantOptOutPopupService
                .open(ParticipantStudiesOptOutComponent as Component, params['participantId'], params['studyId']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
