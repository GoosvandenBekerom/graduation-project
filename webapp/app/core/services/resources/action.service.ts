import { Injectable } from '@angular/core';
import {ResourceService} from './resource-service';
import {Action} from 'core/models/action';
import {HttpClient} from '@angular/common/http';
import {ActionSerializer} from './serializers/action-serializer';

@Injectable({
  providedIn: 'root'
})
export class ActionService extends ResourceService<Action> {

  constructor(private http: HttpClient) {
    super(http, 'action', new ActionSerializer());
  }
}
