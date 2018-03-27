import { Component, OnInit } from '@angular/core';
import { Account, Principal} from '../../shared';
import { ParticipantService } from './participant.service';

@Component({
    selector: 'jhi-participant-settings',
    templateUrl: './participant-settings.component.html',

})
export class ParticipantSettingsComponent implements OnInit {
    participantAccount: any;
    account: Account;
    success: string;

    constructor(
        private principal: Principal,
        private participantService: ParticipantService,
    ) {
    }

    /**
     * This method holds the logic for identifying the account and then calling the required method to find
     * the desired participant. Essentially, it initialises the information needed by the component.
     */
    ngOnInit() {
        this.principal.identity().then((account) => {
            this.participantAccount = this.findParticipant(account);
        });
    }

    /**
     * This method deals with when the submit button is pressed. It saves the changed participant in the
     * database and then sets the success flag to OK
     */
    save() {
        this.success = 'OK';
        this.participantService.update(this.participantAccount).subscribe((participant) => {
            this.participantAccount = participant
        });
    }

    /**
     * This method finds the participant object from the database using the account object and then
     * saves this to be used throughout the HTML
     * @param account - Account object of the user who has logged in
     */
    findParticipant(account) {
        this.participantService.findByEmail(account.email).subscribe((participant) => {
            this.participantAccount = participant
        });
    }
}
