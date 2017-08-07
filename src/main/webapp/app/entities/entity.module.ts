import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { QualOptStudyModule } from './study/study.module';
import { QualOptEmailModule } from './email/email.module';
import { QualOptParticipantModule } from './participant/participant.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        QualOptStudyModule,
        QualOptEmailModule,
        QualOptParticipantModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptEntityModule {}
