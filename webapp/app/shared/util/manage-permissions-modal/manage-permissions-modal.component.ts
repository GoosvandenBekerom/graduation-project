import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import { Resource } from '../../../core/models/resource';
import { UserGroup } from '../../../core/models/user-group';
import { UserGroupService } from '../../../core/services/resources/user-group.service';
import { SecurityGroup } from '../../../core/models/security-group';

@Component({
  selector: 'app-manage-permissions-modal',
  templateUrl: './manage-permissions-modal.component.html',
  styleUrls: ['./manage-permissions-modal.component.css']
})
export class ManagePermissionsModalComponent implements OnInit, OnChanges {

  @Input() open: boolean;
  @Output() openChange = new EventEmitter();

  @Input() entityType: string;

  @Input() entity: Resource;
  @Output() entityChange = new EventEmitter();

  @Output() submit = new EventEmitter<UserGroup[]>();

  userGroups: UserGroup[];
  formGroups: {group: UserGroup, checked: boolean}[];

  constructor(private userGroupService: UserGroupService) {}

  ngOnInit() {
    this.formGroups = [];
  }

  ngOnChanges(_changes: SimpleChanges) {
    if (!this.entity) { return; }

    this.userGroupService.getAll().subscribe(groups => {
      this.userGroups = groups;
      this.formGroups = [];
      this.userGroups.forEach(ug => {
        const isChecked = this.entity.securityGroups
          ? this.entity.securityGroups.findIndex((sg: SecurityGroup) => sg.id === ug.id) !== -1
          : false;
        this.formGroups.push({group: ug, checked: isChecked});
      });
    });
  }

  onClose() {
    this.openChange.emit(false);
  }

  onSubmit() {
    this.entityChange.emit(this.entity);
    this.submit.emit(this.formGroups.filter(g => g.checked).map(g => g.group));
    this.onClose();
  }
}
