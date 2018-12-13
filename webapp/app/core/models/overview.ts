import {Resource} from './resource';
import {OverviewColumn} from './overview-column';

export class Overview extends Resource {
  name: string;
  query: string;
  pageSize = 15;
  refreshEnabled: boolean;
  refreshRate = 30;
  countColumn: string;
  data: any[];
  columns: OverviewColumn[];
}
