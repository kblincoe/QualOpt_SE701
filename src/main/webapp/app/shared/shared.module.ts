import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';

import {
    QualOptSharedLibsModule,
    QualOptSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    Principal,
    HasAnyAuthorityDirective,
    JhiSocialComponent,
    SocialService,
    JhiLoginModalComponent,
    AutofocusDirective
} from './';

@NgModule({
    imports: [QualOptSharedLibsModule, QualOptSharedCommonModule],
    declarations: [
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        AutofocusDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        SocialService,
        UserService,
        DatePipe,
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        QualOptSharedCommonModule,
        JhiSocialComponent,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe,
        AutofocusDirective
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QualOptSharedModule {}
