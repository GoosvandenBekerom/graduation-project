import { Component, OnInit } from '@angular/core';
import {SnotifyService} from 'ng-snotify';
import { Overview } from '../../../core/models/overview';
import { OverviewService } from '../../../core/services/resources/overview.service';
import { handleRestError } from '../../../globals';
import { UserGroup } from '../../../core/models/user-group';

@Component({
  selector: 'app-overview-editor',
  templateUrl: './overview-editor.component.html',
  styleUrls: ['./overview-editor.component.css']
})
export class OverviewEditorComponent implements OnInit {

  overviews: Overview[];

  overviewLoadingState: boolean;

  createModalActive: boolean;
  updateModalActive: boolean;
  deleteModalActive: boolean;
  editColumnsModalActive: boolean;
  managePermissionsModalActive: boolean;

  overviewCreate: Overview;
  overviewUpdate: Overview;
  overviewDelete: Overview;
  overviewEditColumns: Overview;
  overviewManagePermissions: Overview;

  constructor(
    private service: OverviewService,
    private notify: SnotifyService
  ) { }

  ngOnInit() {
    this.service.getAll().subscribe(overviews => this.overviews = overviews, err => handleRestError(err, this.notify));

    this.overviewLoadingState = false;

    this.createModalActive = false;
    this.updateModalActive = false;
    this.deleteModalActive = false;
    this.editColumnsModalActive = false;

    this.overviewCreate = new Overview();
    this.overviewUpdate = new Overview();
    this.overviewDelete = new Overview();
    this.overviewEditColumns = new Overview();
  }

  /* Row action handlers */
  onEdit(overview: Overview) {
    this.openUpdateModal(overview);
  }

  onEditColumns(overview: Overview) {
    this.openEditColumnsModal(overview);
  }

  onManagePermissions(overview: Overview) {
    this.openManagePermissionsModal(overview);
  }

  onDelete(overview: Overview) {
    this.openDeleteModal(overview);
  }

  /* Create Overview Modal */
  openCreateModal() {
    this.overviewCreate = new Overview();
    this.createModalActive = true;
  }

  closeCreateModal() {
    this.createModalActive = false;
  }

  onSubmitCreateModal() {
    this.overviewLoadingState = true;
    this.service.create(this.overviewCreate).subscribe(newOverview => {
      this.overviews.push(newOverview);
      this.overviewLoadingState = false;
    }, err => handleRestError(err, this.notify));
    this.closeCreateModal();
  }

  /* Update Overview Modal */
  openUpdateModal(overview: Overview) {
    this.overviewUpdate = Object.assign({}, overview);
    this.updateModalActive = true;
  }

  closeUpdateModal() {
    this.updateModalActive = false;
  }

  onSubmitUpdateModal() {
    this.service.update(this.overviewUpdate).subscribe(updatedOverview => {
      const index = this.overviews.findIndex(o => o.id === this.overviewUpdate.id);
      this.overviews[index] = updatedOverview;
    });
    this.closeUpdateModal();
  }

  /* Delete Overview Modal */
  openDeleteModal(overview: Overview) {
    this.overviewDelete = Object.assign({}, overview);
    this.deleteModalActive = true;
  }

  closeDeleteModal() {
    this.deleteModalActive = false;
  }

  onYesDeleteModal() {
    const deletedId = this.overviewDelete.id;
    this.service.delete(deletedId).subscribe(() => {
      const index = this.overviews.findIndex(o => o.id === deletedId);
      this.overviews.splice(index, 1);
    });
    this.closeDeleteModal();
  }

  /* Edit Overview Columns Modal */
  openEditColumnsModal(overview: Overview) {
    this.overviewEditColumns = overview;
    this.editColumnsModalActive = true;
  }

  /* Manage Permissions Modal */
  openManagePermissionsModal(overview: Overview) {
    this.overviewManagePermissions = overview;
    this.managePermissionsModalActive = true;
  }

  onSubmitManagePermissions(userGroups: UserGroup[]) {
    this.service.updatePermissions(this.overviewManagePermissions, userGroups).subscribe(updatedOverview => {
      const index = this.overviews.findIndex(o => o.id === this.overviewManagePermissions.id);
      this.overviews[index] = updatedOverview;
    });
  }
}
