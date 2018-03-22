import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
import {
    ParticipantService,
    ParticipantPopupService,
    ParticipantComponent,
    ParticipantDetailComponent,
    ParticipantDialogComponent,
    ParticipantPopupComponent,
    ParticipantDeletePopupComponent,
    ParticipantDeleteDialogComponent,
    participantRoute,
    participantPopupRoute,
} from './';
import { ParticipantPipe } from './participant.pipe';
import { GitHubBackendCall } from './githubAPI/github-backend-call.service';
import { GitHubQueryDialogComponent, GitHubQueryPopupComponent } from './githubAPI/participant-github-query-dialog.component';
import { GitHubPopupService } from './githubAPI/participant-github-popup.service';

const ENTITY_STATES = [
    ...participantRoute,
    ...participantPopupRoute
];

@NgModule({
    imports: [
        QualOptSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ParticipantComponent,
        ParticipantDetailComponent,
        ParticipantDialogComponent,
        ParticipantDeleteDialogComponent,
        ParticipantPopupComponent,
        ParticipantDeletePopupComponent,
        ParticipantPipe,
        GitHubQueryDialogComponent,
        GitHubQueryPopupComponent
    ],
    entryComponents: [
        ParticipantComponent,
        ParticipantDialogComponent,
        ParticipantPopupComponent,
        ParticipantDeleteDialogComponent,
        ParticipantDeletePopupComponent,
        GitHubQueryDialogComponent,
        GitHubQueryPopupComponent
    ],
    providers: [
        ParticipantService,
        ParticipantPopupService,
        GitHubPopupService,
        GitHubBackendCall
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptParticipantModule {}
