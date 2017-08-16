import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EmailComponent } from './email.component';
import { EmailDetailComponent } from './email-detail.component';
import { EmailPopupComponent } from './email-dialog.component';
import { EmailDeletePopupComponent } from './email-delete-dialog.component';

export const emailRoute: Routes = [
    {
        path: 'email',
        component: EmailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Emails'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'email/:id',
        component: EmailDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Emails'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const emailPopupRoute: Routes = [
    {
        path: 'email-new',
        component: EmailPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Emails'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'email/:id/edit',
        component: EmailPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Emails'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'email/:id/delete',
        component: EmailDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Emails'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
