import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
import { QualOptAdminModule } from '../../admin/admin.module';
import {
    StudyService,
    StudyPopupService,
    StudyComponent,
    StudyDetailComponent,
    StudyDialogComponent,
    StudyPopupComponent,
    StudyDeletePopupComponent,
    StudyDeleteDialogComponent,
    StudyConfirmToAllDialogComponent,
    StudyConfirmToNewDialogComponent,
    StudyConfirmToAllPopupComponent,
    StudyConfirmToNewPopupComponent,
    studyRoute,
    studyPopupRoute,
    StudyInfoComponent
} from './';

const ENTITY_STATES = [
    ...studyRoute,
    ...studyPopupRoute,
];

@NgModule({
    imports: [
        QualOptSharedModule,
        QualOptAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StudyComponent,
        StudyDetailComponent,
        StudyDialogComponent,
        StudyDeleteDialogComponent,
        StudyPopupComponent,
        StudyDeletePopupComponent,
        StudyConfirmToAllDialogComponent,
        StudyConfirmToNewDialogComponent,
        StudyConfirmToAllPopupComponent,
        StudyConfirmToNewPopupComponent,
        StudyInfoComponent
    ],
    entryComponents: [
        StudyComponent,
        StudyDialogComponent,
        StudyPopupComponent,
        StudyDeleteDialogComponent,
        StudyDeletePopupComponent,
        StudyConfirmToAllDialogComponent,
        StudyConfirmToNewDialogComponent,
        StudyConfirmToAllPopupComponent,
        StudyConfirmToNewPopupComponent,
        StudyInfoComponent
    ],
    providers: [
        StudyService,
        StudyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptStudyModule {}
