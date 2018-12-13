import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { ClarityModule, ClrFormsNextModule } from '@clr/angular';

import { OverviewRoutingModule } from './overview-routing.module';
import { OverviewComponent } from './overview.component';

@NgModule({
  imports: [
    CommonModule,
    OverviewRoutingModule,
    SharedModule,
    ClarityModule,
    ClrFormsNextModule,
    FormsModule
  ],
  declarations: [
    OverviewComponent
  ],
  exports: [
    OverviewComponent
  ]
})
export class OverviewModule { }
