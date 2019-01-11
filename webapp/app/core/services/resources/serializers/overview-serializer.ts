import {Serializer} from './serializer';
import { Overview } from '../../../models/overview';

export class OverviewSerializer implements Serializer<Overview> {
    toJson = ({name, query, pageSize, autoRefreshFilters, refreshEnabled, refreshRate, countColumn}: Overview) =>
      ({name, query, pageSize, autoRefreshFilters, refreshEnabled, refreshRate, countColumn})
}
