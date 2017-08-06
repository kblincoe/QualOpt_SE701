import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
import {
    EmailService,
    EmailPopupService,
    EmailComponent,
    EmailDetailComponent,
    EmailDialogComponent,
    EmailPopupComponent,
    EmailDeletePopupComponent,
    EmailDeleteDialogComponent,
    emailRoute,
    emailPopupRoute,
} from './';

const ENTITY_STATES = [
    ...emailRoute,
    ...emailPopupRoute,
];

@NgModule({
    imports: [
        QualOptSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EmailComponent,
        EmailDetailComponent,
        EmailDialogComponent,
        EmailDeleteDialogComponent,
        EmailPopupComponent,
        EmailDeletePopupComponent,
    ],
    entryComponents: [
        EmailComponent,
        EmailDialogComponent,
        EmailPopupComponent,
        EmailDeleteDialogComponent,
        EmailDeletePopupComponent,
    ],
    providers: [
        EmailService,
        EmailPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptEmailModule {}
