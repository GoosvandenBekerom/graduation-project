import {Serializer} from './serializer';
import { Overview } from 'core/models/overview';

export class OverviewSerializer implements Serializer<Overview> {
    toJson = ({name, query, pageSize, refreshEnabled, refreshRate, countColumn}: Overview) =>
      ({name, query, pageSize, refreshEnabled, refreshRate, countColumn})
}
