import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Participant } from './participant.model';
import { ParticipantPopupService } from './participant-popup.service';
import { ParticipantService } from './participant.service';

@Component({
    selector: 'jhi-participant-delete-dialog',
    templateUrl: './participant-delete-dialog.component.html'
})
export class ParticipantDeleteDialogComponent {

    participant: Participant;

    constructor(
        private participantService: ParticipantService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.participantService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'participantListModification',
                content: 'Deleted an participant'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-participant-delete-popup',
    template: ''
})
export class ParticipantDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private participantPopupService: ParticipantPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.participantPopupService
                .open(ParticipantDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
