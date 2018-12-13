import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {SnotifyService} from 'ng-snotify';
import { Action } from '../../../core/models/action';
import { ActionType } from '../../../core/models/actionType';
import { Overview } from '../../../core/models/overview';
import { ActionService } from '../../../core/services/resources/action.service';
import { OverviewService } from '../../../core/services/resources/overview.service';
import { UserGroup } from '../../../core/models/user-group';
import { handleRestError } from '../../../globals';

@Component({
  selector: 'app-action-editor',
  templateUrl: './action-editor.component.html',
  styleUrls: ['./action-editor.component.css']
})
export class ActionEditorComponent implements OnInit {

  actions: Action[];
  types = Object.keys(ActionType);
  actionType = ActionType;

  overviews$: Observable<Overview[]>;

  createModalActive: boolean;
  updateModalActive: boolean;
  deleteModalActive: boolean;
  managePermissionsModalActive: boolean;

  actionCreate: Action;
  actionUpdate: Action;
  actionDelete: Action;
  actionPermissionsModal: Action;

  constructor(
    private service: ActionService,
    private overviewService: OverviewService,
    private notify: SnotifyService
  ) { }

  ngOnInit() {
    this.service.getAll().subscribe(actions => this.actions = actions, err => handleRestError(err, this.notify));
    this.types = this.types.slice(this.types.length / 2);
    this.overviews$ = this.overviewService.getAll();
    this.createModalActive = false;
    this.updateModalActive = false;
    this.deleteModalActive = false;
    this.managePermissionsModalActive = false;
    this.actionCreate = new Action();
    this.actionUpdate = new Action();
    this.actionPermissionsModal = new Action();
  }

  /* Row action handlers  */
  onEdit(action: Action) {
    this.openUpdateModal(action);
  }

  onManagePermissions(action: Action) {
    this.openManagePermissions(action);
  }

  onDelete(action: Action) {
    this.openDeleteModal(action);
  }

  /* Create action modal */
  openCreateModal() {
    this.actionCreate = new Action();
    this.actionCreate.type = ActionType[this.types[0]];
    this.createModalActive = true;
  }

  closeCreateModal() {
    this.createModalActive = false;
  }

  onSubmitCreateModal() {
    this.service.create(this.actionCreate).subscribe(action => {
      this.actions.push(action);
    }, err => handleRestError(err, this.notify));
    this.closeCreateModal();
  }


  /* Update action modal */
  openUpdateModal(action: Action) {
    this.actionUpdate = Object.assign({}, action);
    // @ts-ignore (TS enum is evaluated as its integer value at runtime)
    this.actionUpdate.type = ActionType[this.actionUpdate.type];
    this.updateModalActive = true;
  }

  closeUpdateModal() {
    this.updateModalActive = false;
  }

  onSubmitUpdateModal() {
    this.service.update(this.actionUpdate).subscribe(newAction => {
      const index = this.actions.findIndex(item => item.id === this.actionUpdate.id);
      this.actions[index] = newAction;
    }, err => handleRestError(err, this.notify));
    this.closeUpdateModal();
  }


  /* Delete action modal */
  openDeleteModal(action: Action) {
    this.actionDelete = Object.assign({}, action);
    this.deleteModalActive = true;
  }

  closeDeleteModal() {
    this.deleteModalActive = false;
  }

  onYesDeleteModal() {
    const deletedId = this.actionDelete.id;
    this.service.delete(deletedId).subscribe(() => {
      const index = this.actions.findIndex(item => item.id === deletedId);
      this.actions.splice(index, 1);
    }, err => handleRestError(err, this.notify));
    this.closeDeleteModal();
  }

  /* Manage permissions modal */
  openManagePermissions(action: Action) {
    this.actionPermissionsModal = action;
    this.managePermissionsModalActive = true;
  }

  onSubmitManagePermissions(userGroups: UserGroup[]) {
    this.service.updatePermissions(this.actionPermissionsModal, userGroups).subscribe(updatedAction => {
      const index = this.actions.findIndex(a => a.id === this.actionPermissionsModal.id);
      this.actions[index] = updatedAction;
    });
  }
}
