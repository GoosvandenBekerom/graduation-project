import {Component, Input, OnInit} from '@angular/core';
import {ClrDatagridFilterInterface} from '@clr/angular';
import {CustomFilterProperties} from '../custom-filter-properties';
import {Subject} from 'rxjs';
import { ComparisonType, FilterStructure } from '../../../core/util/FilterStructure';

@Component({
  selector: 'app-numeric-filter',
  templateUrl: './numeric-filter.component.html',
  styleUrls: ['./numeric-filter.component.css']
})
export class NumericFilterComponent implements OnInit, ClrDatagridFilterInterface<number>, CustomFilterProperties {

  comparisonTypes = ComparisonType;
  types = [
    '=',
    '<',
    '>',
    '<=',
    '>=',
    '!=',
  ];

  @Input() property: string;
  value = new FilterStructure();

  @Input() autoRefresh: boolean;

  counter = 0;

  changes = new Subject<any>();

  ngOnInit() {
    this.value.filters.push({id: this.counter++, type: this.types[0], value: ''});
  }

  accepts(item: number): boolean { return true; }
  isActive(): boolean { return this.value.filters.findIndex(f => !!f.value) > -1; }

  updateFilter() {
    this.changes.next();
  }

  clear() {
    this.value.filters = [];
    this.value.filters.push({id: this.counter++, type: this.types[0], value: ''});
    this.updateFilter();
  }

  onAddButtonClick() {
    this.value.filters.push({id: this.counter++, type: this.types[0], value: ''});
  }

  onRemoveButtonClick(id: number) {
    const index = this.value.filters.findIndex(f => f.id === id);
    if (index > -1) { this.value.filters.splice(index, 1); }
  }
}
