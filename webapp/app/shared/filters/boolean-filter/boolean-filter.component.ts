import {Component, Input, OnInit} from '@angular/core';
import {ClrDatagridFilterInterface} from '@clr/angular';
import {CustomFilterProperties} from '../custom-filter-properties';
import {Subject} from 'rxjs';
import { FilterStructure } from '../../../core/util/FilterStructure';

@Component({
  selector: 'app-boolean-filter',
  templateUrl: './boolean-filter.component.html',
  styleUrls: ['./boolean-filter.component.css']
})
export class BooleanFilterComponent implements OnInit, ClrDatagridFilterInterface<any>, CustomFilterProperties {

  @Input() property: string;
  value = new FilterStructure();

  filterEnabled: boolean;

  changes = new Subject<any>();

  ngOnInit() {
    this.value.filters.push({id: 1, value: false, type: 'BOOLEAN'});
  }

  accepts(item: any): boolean { return true; }

  isActive(): boolean { return this.filterEnabled; }

  updateFilter(): void {
    this.changes.next();
  }
}
