import { Serializer } from './serializer';
import { Action } from 'core/models/action';
import { ActionType } from 'core/models/actionType';

export class ActionSerializer implements Serializer<Action> {
    toJson({type, url, overviewId, path}: Action) {
        switch (type) {
            case ActionType.External_Url:
                return {type, url};
            case ActionType.Overview:
                return {type, overviewId};
            case ActionType.Path:
                return {type, path};
        }
    }
}
