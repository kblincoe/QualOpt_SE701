import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QualOptSharedModule } from '../../shared';

import { AboutComponent } from './about.component'
import { ABOUT_ROUTE } from './about.route';

/**
 * Adding the routes and components to the module, so that it can be a part of the main application.
 */
@NgModule({
    imports: [
        QualOptSharedModule,
        RouterModule.forRoot([ ABOUT_ROUTE ], { useHash: true })
    ],
    declarations: [
        AboutComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptAboutModule {}
