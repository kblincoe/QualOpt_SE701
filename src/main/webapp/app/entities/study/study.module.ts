import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
import { QualOptAdminModule } from '../../admin/admin.module';
import {
    StudyService,
    StudyPopupService,
    StudyComponent,
    StudyDetailComponent,
    StudyDialogCreateComponent,
    StudyDialogEditComponent,
    StudyPopupCreateComponent,
    StudyPopupEditComponent,
    StudyDeletePopupComponent,
    StudyDeleteDialogComponent,
    studyRoute,
    studyPopupRoute,
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
        StudyDialogCreateComponent,
        StudyDialogEditComponent,
        StudyDeleteDialogComponent,
        StudyPopupCreateComponent,
        StudyPopupEditComponent,
        StudyDeletePopupComponent,
    ],
    entryComponents: [
        StudyComponent,
        StudyDialogCreateComponent,
        StudyDialogEditComponent,
        StudyPopupCreateComponent,
        StudyPopupEditComponent,
        StudyDeleteDialogComponent,
        StudyDeletePopupComponent,
    ],
    providers: [
        StudyService,
        StudyPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptStudyModule {}
