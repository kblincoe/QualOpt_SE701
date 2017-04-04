import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { StudyComponent } from './study.component';
import { StudyDetailComponent } from './study-detail.component';
import { StudyPopupComponent } from './study-dialog.component';
import { StudyDeletePopupComponent } from './study-delete-dialog.component';

import { Principal } from '../../shared';


export const studyRoute: Routes = [
  {
    path: 'study',
    component: StudyComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Studies'
    }
  }, {
    path: 'study/:id',
    component: StudyDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Studies'
    }
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
    outlet: 'popup'
  },
  {
    path: 'study/:id/edit',
    component: StudyPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Studies'
    },
    outlet: 'popup'
  },
  {
    path: 'study/:id/delete',
    component: StudyDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Studies'
    },
    outlet: 'popup'
  }
];
