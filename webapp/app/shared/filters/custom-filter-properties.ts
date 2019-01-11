import { FilterStructure } from '../../core/util/FilterStructure';


export interface CustomFilterProperties {
  property: string;
  value: FilterStructure;
  autoRefresh: boolean;

  updateFilter(): void;
}
