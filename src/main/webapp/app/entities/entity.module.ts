import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { QualOptStudyModule } from './study/study.module';
import { QualOptParticipantModule } from './participant/participant.module';
import { QualOptAboutModule } from './about/about.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        QualOptStudyModule,
        QualOptParticipantModule,
        QualOptAboutModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptEntityModule {}
