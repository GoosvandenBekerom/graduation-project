import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {flatMap, map, toArray} from 'rxjs/operators';
import { Overview } from '../../../core/models/overview';
import { SecurityGroup } from '../../../core/models/security-group';
import { OverviewService } from '../../../core/services/resources/overview.service';
import { UserGroupService } from '../../../core/services/resources/user-group.service';
import { OverviewColumn } from '../../../core/models/overview-column';
import { ColumnPermissionsFormGroup } from '../column-permissions-dropdown/column-permissions-dropdown.component';

@Component({
  selector: 'app-edit-columns-modal',
  templateUrl: './edit-columns-modal.component.html',
  styleUrls: ['./edit-columns-modal.component.css']
})
export class EditColumnsModalComponent implements OnInit, OnChanges {

  @Input() overview: Overview;
  @Output() overviewChange = new EventEmitter();

  @Input() open: boolean;
  @Output() openChange = new EventEmitter();

  @Output() submit = new EventEmitter<Overview>();

  userGroups$: Observable<SecurityGroup[]>;

  constructor(
    private service: OverviewService,
    private userGroupService: UserGroupService,
  ) {}

  ngOnInit() {
    this.userGroups$ = this.userGroupService.getAll()
      .pipe(flatMap(ugs => ugs), map(ug => new SecurityGroup(ug.id)), toArray());
  }

  ngOnChanges() {
    if (!this.overview || !this.overview.columns) { return; }
    this.overview.columns.sort((a, b) => a.sequenceIndex - b.sequenceIndex);
  }

  onClose() {
    this.openChange.emit(false);
  }

  onSubmit() {
    this.service.updateColumns(this.overview).subscribe(updatedOverview => {
      this.overviewChange.emit(updatedOverview);
      this.onClose();
    });
  }

  onChangeColumnPermissions(column: OverviewColumn, fg: ColumnPermissionsFormGroup) {
    const index = column.securityGroups.findIndex(sg => sg.id === fg.group.id);

    if (index === -1) {
      if (!fg.checked) { return; }

      column.securityGroups.push(fg.group);
    } else {
      if (fg.checked) { return; }

      column.securityGroups.splice(index, 1);
    }
    this.service.updateColumnPermissions(column.id, column.securityGroups).subscribe();
  }
}
