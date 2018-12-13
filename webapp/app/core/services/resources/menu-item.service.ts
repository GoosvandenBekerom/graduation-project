import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ResourceService} from './resource-service';
import {MenuItemSerializer} from './serializers/menu-item-serializer';
import {API_BASE_URL} from 'core/constants';
import {MenuItem} from 'core/models/menu-item';

@Injectable({
  providedIn: 'root'
})
export class MenuItemService extends ResourceService<MenuItem> {
  constructor(private http: HttpClient) {
    super(http, 'menuItem', new MenuItemSerializer());
  }

  getParent(item: MenuItem): Observable<MenuItem> {
    if (item.depth === 0) { return Observable.create(undefined); }
    return this.http.get<MenuItem>(`${API_BASE_URL}/${this.endpoint}/${item.id}/parent`);
  }

  public setParent(menuItemId: number, parentId: number) {
    const body = new HttpParams().set('parentId', parentId.toString());
    return this.http.put<MenuItem>(`${API_BASE_URL}/${this.endpoint}/${menuItemId}/parent`, body);
  }

  public setAction(menuItemId: number, actionId: number) {
    const body = new HttpParams().set('actionId', actionId.toString());
    return this.http.put<MenuItem>(`${API_BASE_URL}/${this.endpoint}/${menuItemId}/action`, body);
  }

  public deleteParent(id: number) {
    return this.http.delete(`${API_BASE_URL}/${this.endpoint}/${id}/parent`);
  }

  public deleteAction(id: number) {
    return this.http.delete(`${API_BASE_URL}/${this.endpoint}/${id}/action`);
  }
}
