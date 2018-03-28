import { Component, OnInit } from '@angular/core';
import { Account, Principal} from '../../shared';
import { ParticipantService } from './participant.service';
import { Participant } from './participant.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs/Rx';
import { access } from 'fs';

@Component({
    selector: 'jhi-participant-studies',
    templateUrl: './participant-studies.component.html',

})
export class ParticipantStudiesComponent implements OnInit {
    participantAccount: Participant;
    account: Account;
    eventSubscriber: Subscription;

    constructor(
        private principal: Principal,
        private participantService: ParticipantService,
        private eventManager: JhiEventManager,
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            this.findParticipant(account);
        });

        this.registerChangeInParticipants();
    }

    /**
     * This is a method to set up the event listener for when the participantStudiesListModification message
     * is broadcasted. It responds to the broadcast with a function call to update the participant from the
     * database to update the information on the HTML
     */
    registerChangeInParticipants() {
        this.eventSubscriber = this.eventManager.subscribe('participantStudiesListModification', (response) => this.findParticipant(this.account));
    }

    /**
     * This method cleans up the handlers when this component is destroyed.
     */
    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    findParticipant(account) {
        this.participantService.findByEmail(account.email).subscribe((participant) => {
            this.participantAccount = participant;
        });
    }
}
