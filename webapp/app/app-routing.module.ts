import {InjectionToken, NgModule} from '@angular/core';
import {ActivatedRouteSnapshot, RouterModule, Routes} from '@angular/router';
import { PATH_ROUTE_OVERVIEW, PATH_ROUTE_EDITOR } from 'core/constants';
import { AuthGuard } from 'core/guards/auth.guard';
import { NotFoundComponent } from 'views/not-found/not-found.component';
import { HomeComponent } from 'views/home/home.component';

const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');

const routes: Routes = [

    /* Home */
    {
        path: '',
        component: HomeComponent,
        canActivate: [AuthGuard],
    },

    /* Login */
    {
        path: 'login',
        loadChildren: 'views/login/login.module#LoginModule'
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
        loadChildren: 'views/overview/overview.module#OverviewModule'
    },

    /* Editor page */
    {
        path: PATH_ROUTE_EDITOR,
        loadChildren: 'views/editor/editor.module#EditorModule'
    },

    /* Add custom routes below */



    /* 404 Not Found resolver */
    { path: '**', component: NotFoundComponent }
];

@NgModule({
    providers: [
        {
            provide: externalUrlProvider,
            useValue: function (route: ActivatedRouteSnapshot) {
                const url = route.paramMap.get('url');
                window.open(url, '_self');
            },
        }
    ],
    imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
    exports: [RouterModule]
})
export class AppRoutingModule { }
