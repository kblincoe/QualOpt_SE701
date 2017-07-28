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

const ENTITY_STATES = [
    ...participantRoute,
    ...participantPopupRoute,
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
    ],
    entryComponents: [
        ParticipantComponent,
        ParticipantDialogComponent,
        ParticipantPopupComponent,
        ParticipantDeleteDialogComponent,
        ParticipantDeletePopupComponent,
    ],
    providers: [
        ParticipantService,
        ParticipantPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptParticipantModule {}
