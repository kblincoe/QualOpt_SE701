import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Participant } from './participant.model';
import { ParticipantService } from './participant.service';

@Component({
    selector: 'jhi-participant-detail',
    templateUrl: './participant-detail.component.html'
})
export class ParticipantDetailComponent implements OnInit, OnDestroy {

    participant: Participant;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private participantService: ParticipantService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInParticipants();
    }

    load(id) {
        this.participantService.find(id).subscribe((participant) => {
            this.participant = participant;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInParticipants() {
        this.eventSubscriber = this.eventManager.subscribe(
            'participantListModification',
            (response) => this.load(this.participant.id)
        );
    }
}
