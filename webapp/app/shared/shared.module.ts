import { ClrFormsNextModule, ClrFormsModule, ClarityModule } from '@clr/angular';
import { FormsModule } from '@angular/forms';
import { PlusButtonComponent } from './util/plus-button/plus-button.component';
import { ManagePermissionsModalComponent } from './util/manage-permissions-modal/manage-permissions-modal.component';
import { ColumnPermissionsDropdownComponent } from './util/column-permissions-dropdown/column-permissions-dropdown.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DevConsoleComponent } from './dev-console/dev-console.component';
import { BooleanFilterComponent } from './filters/boolean-filter/boolean-filter.component';
import { DropdownFilterComponent } from './filters/dropdown-filter/dropdown-filter.component';
import { NumericFilterComponent } from './filters/numeric-filter/numeric-filter.component';
import { VarcharFilterComponent } from './filters/varchar-filter/varchar-filter.component';
import { EditColumnsModalComponent } from './util/edit-columns-modal/edit-columns-modal.component';
import { RemoveButtonComponent } from './util/remove-button/remove-button.component';
import {NotFoundComponent} from './not-found/not-found.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ClrFormsNextModule,
    ClrFormsModule,
    ClarityModule
  ],
  declarations: [
    DevConsoleComponent,
    BooleanFilterComponent,
    DropdownFilterComponent,
    NumericFilterComponent,
    VarcharFilterComponent,
    ColumnPermissionsDropdownComponent,
    EditColumnsModalComponent,
    ManagePermissionsModalComponent,
    PlusButtonComponent,
    RemoveButtonComponent,
    NotFoundComponent
  ],
  exports: [
    DevConsoleComponent,
    BooleanFilterComponent,
    DropdownFilterComponent,
    NumericFilterComponent,
    VarcharFilterComponent,
    ColumnPermissionsDropdownComponent,
    EditColumnsModalComponent,
    ManagePermissionsModalComponent,
    PlusButtonComponent,
    RemoveButtonComponent
  ]
})
export class SharedModule { }
