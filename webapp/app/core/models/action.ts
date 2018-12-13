import {Resource} from './resource';
import {ActionType} from './actionType';

export class Action extends Resource {
    type: ActionType;
    url: string;
    overviewId: number;
    path: string;
}

