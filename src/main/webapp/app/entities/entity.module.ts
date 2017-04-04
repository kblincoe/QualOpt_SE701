import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { QualOpt2ResearcherModule } from './researcher/researcher.module';
import { QualOpt2StudyModule } from './study/study.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        QualOpt2ResearcherModule,
        QualOpt2StudyModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOpt2EntityModule {}
