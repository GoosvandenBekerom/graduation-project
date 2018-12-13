import {Resource} from '../../../models/resource';

export interface Serializer<T extends Resource> {
    toJson(resource: T): any;
}
