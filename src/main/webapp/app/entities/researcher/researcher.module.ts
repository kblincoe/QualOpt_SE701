import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOpt2SharedModule } from '../../shared';

import {
    ResearcherService,
    ResearcherPopupService,
    ResearcherComponent,
    ResearcherDetailComponent,
    ResearcherDialogComponent,
    ResearcherPopupComponent,
    ResearcherDeletePopupComponent,
    ResearcherDeleteDialogComponent,
    researcherRoute,
    researcherPopupRoute,
} from './';

let ENTITY_STATES = [
    ...researcherRoute,
    ...researcherPopupRoute,
];

@NgModule({
    imports: [
        QualOpt2SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ResearcherComponent,
        ResearcherDetailComponent,
        ResearcherDialogComponent,
        ResearcherDeleteDialogComponent,
        ResearcherPopupComponent,
        ResearcherDeletePopupComponent,
    ],
    entryComponents: [
        ResearcherComponent,
        ResearcherDialogComponent,
        ResearcherPopupComponent,
        ResearcherDeleteDialogComponent,
        ResearcherDeletePopupComponent,
    ],
    providers: [
        ResearcherService,
        ResearcherPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOpt2ResearcherModule {}
