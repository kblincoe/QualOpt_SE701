import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StudyComponent } from './study.component';
import { StudyDetailComponent } from './study-detail.component';
import { StudyPopupCreateComponent } from './study-dialog-create.component';
import { StudyPopupEditComponent } from './study-dialog-edit.component';
import { StudyDeletePopupComponent } from './study-delete-dialog.component';

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
    }
];

export const studyPopupRoute: Routes = [
    {
        path: 'study-new',
        component: StudyPopupCreateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'study/:id/edit',
        component: StudyPopupEditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Studies'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
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
    }
];
