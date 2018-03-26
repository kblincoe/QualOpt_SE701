import { Route } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SettingsComponent } from '../settings/settings.component';

export const passwordRoute: Route = {
    path: 'password',
    component: SettingsComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Password'
    },
    canActivate: [UserRouteAccessService]
};
