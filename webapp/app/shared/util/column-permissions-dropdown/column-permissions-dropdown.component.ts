import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import { OverviewColumn } from 'core/models/overview-column';
import { SecurityGroup } from 'core/models/security-group';

@Component({
  selector: 'app-column-permissions-dropdown',
  templateUrl: './column-permissions-dropdown.component.html',
  styleUrls: ['./column-permissions-dropdown.component.css']
})
export class ColumnPermissionsDropdownComponent implements OnInit, OnChanges {

  @Input() column: OverviewColumn;
  @Input() groups: SecurityGroup[];

  @Output() groupChange = new EventEmitter<ColumnPermissionsFormGroup>();

  formGroups: ColumnPermissionsFormGroup[];

  constructor() {}

  ngOnInit() {
    this.formGroups = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.column || !this.groups) { return; }

    this.formGroups = [];
    this.groups.forEach(g => {
      const isChecked = this.column.securityGroups.findIndex(sg => sg.id === g.id) !== -1;
      this.formGroups.push(new ColumnPermissionsFormGroup(g, isChecked));
    });
  }

  onCheckboxChange(formGroup: ColumnPermissionsFormGroup) {
    this.groupChange.emit(formGroup);
  }
}

export class ColumnPermissionsFormGroup {
  group: SecurityGroup;
  checked: boolean;

  constructor(group: SecurityGroup, checked: boolean) {
    this.group = group;
    this.checked = checked;
  }
}
