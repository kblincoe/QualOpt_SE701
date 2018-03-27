import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ParticipantComponent } from './participant.component';
import { ParticipantDetailComponent } from './participant-detail.component';
import { ParticipantPopupComponent } from './participant-dialog.component';
import { ParticipantDeletePopupComponent } from './participant-delete-dialog.component';
import { GitHubQueryPopupComponent } from './githubAPI/participant-github-query-dialog.component';

export const participantRoute: Routes = [
    {
        path: 'participant',
        component: ParticipantComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Participants'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'participant/:id',
        component: ParticipantDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Participants'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const participantPopupRoute: Routes = [
    {
        path: 'participant-new',
        component: ParticipantPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Participants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'participant/:id/edit',
        component: ParticipantPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Participants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'participant/:id/delete',
        component: ParticipantDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Participants'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'github-query',
        component: GitHubQueryPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_USER'],
            pageTitle: 'GitHub User Search Query'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
