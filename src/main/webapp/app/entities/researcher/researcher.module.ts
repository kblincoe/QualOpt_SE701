import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
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

const ENTITY_STATES = [
    ...researcherRoute,
    ...researcherPopupRoute,
];

@NgModule({
    imports: [
        QualOptSharedModule,
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
export class QualOptResearcherModule {}
