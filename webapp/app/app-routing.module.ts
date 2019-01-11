import {InjectionToken, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, RouterModule, Routes} from '@angular/router';
import { PATH_ROUTE_OVERVIEW, PATH_ROUTE_EDITOR } from './core';
import { AuthGuard } from './core';
import { NotFoundComponent } from './shared';

const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');

const routes: Routes = [

    /* Home */
    {
        path: '',
        loadChildren: './views/home/home.module#HomeModule'
    },

    /* Login */
    {
        path: 'login',
        loadChildren: './views/login/login.module#LoginModule'
    },

    /* External url */
    {
        path: 'redirectExternal',
        resolve: {
            url: externalUrlProvider,
        },
        component: NotFoundComponent, // unused but the route won't work without a component
        canActivate: [AuthGuard]
    },

    /* Data Overview page */
    {
        path: PATH_ROUTE_OVERVIEW,
        loadChildren: './views/overview/overview.module#OverviewModule'
    },

    /* Editor page */
    {
        path: PATH_ROUTE_EDITOR,
        loadChildren: './views/editor/editor.module#EditorModule'
    },

    /* Add custom routes below */



    /* 404 Not Found resolver */
    { path: '**', component: NotFoundComponent }
];

export function externalRedirect(route: ActivatedRouteSnapshot) {
  const url = route.paramMap.get('url');
  window.open(url, '_self');
}

@NgModule({
    providers: [
        {
            provide: externalUrlProvider,
            useValue: externalRedirect,
        }
    ],
    imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
    exports: [RouterModule]
})
export class AppRoutingModule { }
