import { Route } from '@angular/router';

/**
 * Routing for the about page
 */

import { UserRouteAccessService } from '../../shared';
import { AboutComponent } from './about.component';

export const ABOUT_ROUTE: Route = {
    path: 'about',
    component: AboutComponent,
    data: {
        authorities: [],
        pageTitle: 'About'
    }
};
