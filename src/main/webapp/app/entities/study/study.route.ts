import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudyComponent } from './study.component';
import { StudyDetailComponent } from './study-detail.component';
import { StudyPopupComponent } from './study-dialog.component';
import { StudyDeletePopupComponent } from './study-delete-dialog.component';
import { StudyConfirmPopupComponent } from './study-confirm-invitation.component'; 
import { StudyInfoComponent } from './study-info.component';

export const studyRoute: Routes = [
    {
        path: 'study',
        component: StudyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'study/:id',
        component: StudyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'study/:id/info',
        component: StudyInfoComponent,
        data: {
            pageTitle: 'Study Information'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const studyPopupRoute: Routes = [
    {
        path: 'study-new',
        component: StudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'study/:id/edit',
        component: StudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: "study/:id/copy",
        component: StudyPopupComponent,
        data: {
            authorities: ["ROLE_USER"],
            pageTitle: "Studies",
            copy: true
        },
        canActivate: [UserRouteAccessService],
        outlet: "popup"
    },
    {
        path: 'study/:id/delete',
        component: StudyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'study/:id/confirmSend',
        component: StudyConfirmPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
