import { Component, OnInit } from '@angular/core';
import {SnotifyService} from 'ng-snotify';
import {flatMap, map, toArray} from 'rxjs/operators';
import {combineLatest, of} from 'rxjs';
import * as iconList from 'assets/icons.json';
import { MenuItemService } from 'core/services/resources/menu-item.service.js';
import { ActionService } from 'core/services/resources/action.service';
import { MenuItem } from 'core/models/menu-item';
import { Action } from 'core/models/Action';
import { handleRestError } from 'globals';
import { UserGroup } from 'core/models/user-group';
import { DEFAULT_ICON } from 'core/constants';

@Component({
  selector: 'app-menu-editor',
  templateUrl: './menu-editor.component.html',
  styleUrls: ['./menu-editor.component.css']
})
export class MenuEditorComponent implements OnInit {

  menuItems: MenuItem[];
  actions: Action[];

  icons: string[];

  createModalActive: boolean;
  updateModalActive: boolean;
  parentModalActive: boolean;
  actionModalActive: boolean;
  deleteModalActive: boolean;
  deleteParentModalActive: boolean;
  deleteActionModalActive: boolean;
  managePermissionsModalActive: boolean;

  menuItemCreate: MenuItem;
  menuItemUpdate: MenuItem;
  menuItemParent: MenuItem;
  menuItemAction: MenuItem;
  menuItemDelete: MenuItem;
  menuItemPermissionsModal: MenuItem;

  potentialParents: MenuItem[];

  constructor(
    private service: MenuItemService,
    private actionService: ActionService,
    private notify: SnotifyService
  ) {}

  ngOnInit() {
    this.service.getAll().pipe(
      flatMap(items => items),
      flatMap(item => {
        if (item.depth === 0) { return of(item); }

        const parent$ = this.service.getParent(item);
        return combineLatest(parent$).pipe(
          map(parent => ({parent, item})),
          map(value => {
            value.item.parent = value.parent[0];
            return value.item;
          })
        );
      }),
      toArray(),
    ).subscribe(items => this.menuItems = items, err => handleRestError(err, this.notify));

    this.actionService.getAll().subscribe(actions => this.actions = actions, err => handleRestError(err, this.notify));
    this.createModalActive = false;
    this.updateModalActive = false;
    this.parentModalActive = false;
    this.actionModalActive = false;
    this.deleteModalActive = false;
    this.deleteParentModalActive = false;
    this.deleteActionModalActive = false;
    this.managePermissionsModalActive = false;
    this.icons = iconList.default;

    this.menuItemCreate = new MenuItem();
    this.menuItemCreate.icon = DEFAULT_ICON;
    this.menuItemUpdate = new MenuItem();
    this.menuItemUpdate.icon = DEFAULT_ICON;
    this.menuItemPermissionsModal = new MenuItem();
    this.menuItemPermissionsModal.icon = DEFAULT_ICON;

    this.potentialParents = [];
  }


  /* Row option handlers */
  onEdit(item: MenuItem) {
    this.openUpdateModal(item);
  }

  onManagePermissions(item: MenuItem) {
    this.openManagePermissions(item);
  }

  onSetParent(item: MenuItem) {
    this.openParentModal(item);
  }

  onSetAction(item: MenuItem) {
    this.openActionModal(item);
  }

  onDelete(item: MenuItem) {
    this.openDeleteModal(item);
  }

  onDeleteParent(item: MenuItem) {
    this.openDeleteParentModal(item);
  }

  onDeleteAction(item: MenuItem) {
    this.openDeleteActionModal(item);
  }


   /* Create MenuItem Modal */
  openCreateModal() {
    this.menuItemCreate = new MenuItem();
    this.menuItemCreate.icon = DEFAULT_ICON;
    this.createModalActive = true;
  }

  closeCreateModal() {
    this.createModalActive = false;
  }

  onSubmitCreateModal() {
    this.service.create(this.menuItemCreate).subscribe(newItem => {
      newItem.leaf = true;
      this.menuItems.push(newItem);
    });
    this.closeCreateModal();
  }


  /* Update MenuItem Modal */
  openUpdateModal(item: MenuItem) {
    this.menuItemUpdate.id = item.id;
    this.menuItemUpdate.text = item.text;
    this.menuItemUpdate.icon = item.icon || DEFAULT_ICON;
    this.updateModalActive = true;
  }

  closeUpdateModal() {
    this.updateModalActive = false;
  }

  onSubmitUpdateModal() {
    const updatedId = this.menuItemUpdate.id;
    this.service.update(this.menuItemUpdate).subscribe(newItem => {
      const index = this.menuItems.findIndex(item => item.id === updatedId);
      this.menuItems[index] = newItem;
    }, err => handleRestError(err, this.notify));
    this.closeUpdateModal();
  }


  /* Set/Update Parent Modal */
  openParentModal(item: MenuItem) {
    this.menuItemParent = Object.assign({}, item);
    this.setPotentialParents(item);
    if (!this.menuItemParent.parent) {
      this.menuItemParent.parent = Object.assign({}, this.potentialParents[0]) || undefined;
    }
    this.parentModalActive = true;
  }

  private setPotentialParents(item: MenuItem) {
    this.potentialParents = [];
    this.menuItems.forEach(i => {
      const newDepth = i.depth + item.depth + 1;
      if (i != item && i.depth < 2 && newDepth < 3) {
        this.potentialParents.push(i);
      }
    });

    if (this.potentialParents.length < 1) {
      console.error('No potential parents, TODO: catch this somehow');
    }
  }

  closeParentModal() {
    this.parentModalActive = false;
  }

  onSubmitParentModal() {
    this.service.setParent(this.menuItemParent.id, this.menuItemParent.parent.id)
      .subscribe(newItem => {
        const index = this.menuItems.findIndex(item => item.id === this.menuItemParent.id);
        this.menuItems[index] = newItem;
      }, err => handleRestError(err, this.notify));
    this.closeParentModal();
  }


  /* Set/Update Action Modal */
  openActionModal(item: MenuItem) {
    this.menuItemAction = Object.assign({}, item);
    if (!this.menuItemAction.action) {
      this.menuItemAction.action = this.actions[0] || undefined;
    } else {
      this.menuItemAction.action = this.actions.find(a => a.id === this.menuItemAction.action.id);
    }
    this.actionModalActive = true;
  }

  closeActionModal() {
    this.actionModalActive = false;
  }

  onSubmitActionModal() {
    this.service.setAction(this.menuItemAction.id, this.menuItemAction.action.id)
      .subscribe(newItem => {
        const index = this.menuItems.findIndex(item => item.id === this.menuItemAction.id);
        this.menuItems[index] = newItem;
      }, err => handleRestError(err, this.notify));
    this.closeActionModal();
  }


  /* Delete MenuItem Modal */
  openDeleteModal(itemToDelete: MenuItem) {
    this.menuItemDelete = itemToDelete;
    this.deleteModalActive = true;
  }

  closeDeleteModal() {
    this.deleteModalActive = false;
    this.menuItemDelete = undefined;
  }

  onCancelDeleteModal() {
    this.closeDeleteModal();
  }

  onYesDeleteModal() {
    const deletedId = this.menuItemDelete.id;
    this.service.delete(deletedId).subscribe(() => {
      for (let i = 0; i < this.menuItems.length; i++) {
        if (this.menuItems[i].id === deletedId) {
          this.menuItems.splice(i, 1);
          break;
        }
      }
    }, err => handleRestError(err, this.notify));
    this.closeDeleteModal();
  }


  /* Delete Parent Modal */
  openDeleteParentModal(itemToDelete: MenuItem) {
    this.menuItemDelete = Object.assign({}, itemToDelete);
    this.deleteParentModalActive = true;
  }

  closeDeleteParentModal() {
    this.deleteParentModalActive = false;
    this.menuItemDelete = undefined;
  }

  onCancelDeleteParentModal() {
    this.closeDeleteParentModal();
  }

  onYesDeleteParentModal() {
    const deletedId = this.menuItemDelete.id;
    this.service.deleteParent(deletedId).subscribe(() => {
      for (let i = 0; i < this.menuItems.length; i++) {
        if (this.menuItems[i].id === deletedId) {
          this.menuItems[i].parent = undefined;
          this.menuItems[i].depth--;
          break;
        }
      }
    }, err => handleRestError(err, this.notify));
    this.closeDeleteParentModal();
  }


  /* Delete Action Modal */
  openDeleteActionModal(itemToDelete: MenuItem) {
    this.menuItemDelete = Object.assign({}, itemToDelete);
    this.deleteActionModalActive = true;
  }

  closeDeleteActionModal() {
    this.deleteActionModalActive = false;
    this.menuItemDelete = undefined;
  }

  onCancelDeleteActionModal() {
    this.closeDeleteActionModal();
  }

  onYesDeleteActionModal() {
    const deletedId = this.menuItemDelete.id;
    this.service.deleteAction(deletedId).subscribe(() => {
      for (let i = 0; i < this.menuItems.length; i++) {
        if (this.menuItems[i].id === deletedId) {
          this.menuItems[i].action = undefined;
          break;
        }
      }
    }, err => handleRestError(err, this.notify));
    this.closeDeleteActionModal();
  }

  /* Manage permissions modal */
  openManagePermissions(item: MenuItem) {
    this.menuItemPermissionsModal = item;
    this.managePermissionsModalActive = true;
  }

  onSubmitManagePermissions(userGroups: UserGroup[]) {
    this.service.updatePermissions(this.menuItemPermissionsModal, userGroups).subscribe(updatedItem => {
      const index = this.menuItems.findIndex(a => a.id === this.menuItemPermissionsModal.id);
      this.menuItems[index] = updatedItem;
    });
  }
}
