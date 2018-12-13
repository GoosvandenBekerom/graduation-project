import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserGroup} from '../../models/user-group';
import {API_BASE_URL} from '../../constants';

@Injectable({
  providedIn: 'root'
})
export class UserGroupService {

  constructor(private http: HttpClient) { }

  public getAll(): Observable<UserGroup[]> {
    return this.http.get<UserGroup[]>(`${API_BASE_URL}/userGroups`);
  }
}
