import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OverviewComponent } from './overview.component';
import { AuthGuard } from 'core/guards/auth.guard';

const routes: Routes = [
  /* Data Overview page */
  {
    path: ':id',
    component: OverviewComponent,
    runGuardsAndResolvers: 'paramsChange',
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OverviewRoutingModule { }
