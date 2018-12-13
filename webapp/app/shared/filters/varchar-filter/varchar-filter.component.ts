import {Component, Input, OnInit} from '@angular/core';
import {ClrDatagridFilterInterface} from '@clr/angular';
import {Subject} from 'rxjs';
import {CustomFilterProperties} from '../custom-filter-properties';
import { ComparisonType, FilterStructure } from '../../../core/util/FilterStructure';

@Component({
  selector: 'app-varchar-filter',
  templateUrl: './varchar-filter.component.html',
  styleUrls: ['./varchar-filter.component.css']
})
export class VarcharFilterComponent implements OnInit, ClrDatagridFilterInterface<string>, CustomFilterProperties {

  comparisonTypes = ComparisonType;
  types = [
    'LIKE',
    'NOT LIKE',
    '=',
    'IS NULL',
    'IS NOT NULL',
    // "REGEX",
  ];

  @Input() property: string;
  value = new FilterStructure();

  counter = 0;

  changes = new Subject<any>();

  ngOnInit() {
    this.value.filters.push({id: this.counter++, type: this.types[0], value: ''});
  }

  onAddButtonClick() {
    this.value.filters.push({id: this.counter++, type: this.types[0], value: ''});
  }

  onRemoveButtonClick(id: number) {
    const index = this.value.filters.findIndex(f => f.id === id);
    if (index > -1) { this.value.filters.splice(index, 1); }
  }

  accepts(item: string): boolean {return true;}

  isActive(): boolean { return this.value.filters.filter(f => f.type.includes('NULL') || !!f.value).length > 0; }

  updateFilter() {
    this.changes.next();
  }
}
