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
    StudyConfirmDialogComponent,
    StudyConfirmPopupComponent,
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
        StudyConfirmDialogComponent,
        StudyConfirmPopupComponent,
        StudyInfoComponent
    ],
    entryComponents: [
        StudyComponent,
        StudyDialogComponent,
        StudyPopupComponent,
        StudyConfirmDialogComponent,
        StudyDeleteDialogComponent,
        StudyDeletePopupComponent,
        StudyConfirmPopupComponent,
        StudyInfoComponent
    ],
    providers: [
        StudyService,
        StudyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptStudyModule {}
