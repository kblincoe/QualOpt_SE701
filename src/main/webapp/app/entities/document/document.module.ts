import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';
import {
    DocumentService
} from './';

@NgModule({
    imports: [
        QualOptSharedModule
    ],
    providers: [
        DocumentService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptDocumentModule {}