import {Component, Input, OnInit} from '@angular/core';
import {ClrDatagridFilterInterface} from '@clr/angular';
import {CustomFilterProperties} from '../custom-filter-properties';
import {Subject} from 'rxjs';
import { OverviewService } from '../../../core/services/resources/overview.service';
import { OverviewColumn } from '../../../core/models/overview-column';
import { FilterStructure, ComparisonType } from '../../../core/util/FilterStructure';

@Component({
  selector: 'app-dropdown-filter',
  templateUrl: './dropdown-filter.component.html',
  styleUrls: ['./dropdown-filter.component.css']
})
export class DropdownFilterComponent implements OnInit, ClrDatagridFilterInterface<any>, CustomFilterProperties {

  constructor(private service: OverviewService) {}

  @Input() property: string;
  @Input() column: OverviewColumn;
  values: string[];
  type = 'DROPDOWN';
  value = new FilterStructure(ComparisonType.OR);

  filterActive = false;
  counter = 0;

  changes = new Subject<any>();

  ngOnInit() {
    this.service.getColumnFilterValues(this.column.id)
      .subscribe(values => {
        this.values = values;
        this.value.filters.push({id: this.counter++, type: 'DROPDOWN', value: this.values[0]});
      });
  }

  accepts(item: any): boolean { return true; }
  isActive(): boolean { return !!this.value && this.filterActive; }

  updateFilter(): void {
    this.changes.next(this.value);
  }

  onAddButtonClick() {
    this.value.filters.push({id: this.counter++, type: 'DROPDOWN', value: this.values[0] || undefined});
  }

  onRemoveButtonClick(id: number) {
    const index = this.value.filters.findIndex(f => f.id === id);
    if (index > -1) { this.value.filters.splice(index, 1); }
  }
}
