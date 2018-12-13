import { Injectable } from '@angular/core';
import {ResourceService} from './resource-service';
import {HttpClient} from '@angular/common/http';
import {Action} from '../../models/action';
import {ActionSerializer} from './serializers/action-serializer';

@Injectable({
  providedIn: 'root'
})
export class ActionService extends ResourceService<Action> {

  constructor(private http: HttpClient) {
    super(http, 'action', new ActionSerializer());
  }
}
