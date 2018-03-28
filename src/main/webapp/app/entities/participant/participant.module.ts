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
    ParticipantSettingsComponent,
    participantSettingsRoute,
    participantStudiesRoute,
    ParticipantStudiesComponent,
    ParticipantStudiesOptOutComponent,
    ParticipantStudiesOptOutPopupComponent,
    participantStudiesPopupRoute,
    ParticipantOptOutPopupService,
} from './';
import { ParticipantPipe } from './participant.pipe';
import { GitHubBackendCall } from './githubAPI/github-backend-call.service';
import { GitHubQueryDialogComponent, GitHubQueryPopupComponent } from './githubAPI/participant-github-query-dialog.component';
import { GitHubPopupService } from './githubAPI/participant-github-popup.service';

const ENTITY_STATES = [
    ...participantRoute,
    ...participantPopupRoute,
    participantSettingsRoute,
    participantStudiesRoute,
    ...participantStudiesPopupRoute,
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
        GitHubQueryPopupComponent,
        ParticipantSettingsComponent,
        ParticipantStudiesComponent,
        ParticipantStudiesOptOutComponent,
        ParticipantStudiesOptOutPopupComponent,
    ],
    entryComponents: [
        ParticipantComponent,
        ParticipantDialogComponent,
        ParticipantPopupComponent,
        ParticipantDeleteDialogComponent,
        ParticipantDeletePopupComponent,
        GitHubQueryDialogComponent,
        GitHubQueryPopupComponent,
        ParticipantStudiesOptOutComponent,
        ParticipantStudiesOptOutPopupComponent,
    ],
    providers: [
        ParticipantService,
        ParticipantPopupService,
        GitHubPopupService,
        GitHubBackendCall,
        ParticipantOptOutPopupService, 
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptParticipantModule {}
