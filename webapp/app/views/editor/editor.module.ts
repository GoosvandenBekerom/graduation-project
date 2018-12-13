import { SharedModule } from './../../shared/shared.module';
import { CoreModule } from './../../core/core.module';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditorRoutingModule } from './editor-routing.module';
import { EditorComponent } from './editor.component';
import { ActionEditorComponent } from './action-editor/action-editor.component';
import { MenuEditorComponent } from './menu-editor/menu-editor.component';
import { OverviewEditorComponent } from './overview-editor/overview-editor.component';
import { ClarityModule, ClrFormsModule, ClrFormsNextModule } from '@clr/angular';

@NgModule({
  imports: [
    CommonModule,
    EditorRoutingModule,
    SharedModule,
    ClarityModule,
    ClrFormsModule,
    FormsModule
  ],
  declarations: [
    EditorComponent,
    ActionEditorComponent,
    MenuEditorComponent,
    OverviewEditorComponent
  ],
  exports: [
    EditorComponent
  ]
})
export class EditorModule { }
