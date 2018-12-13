import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ResourceService} from './resource-service';
import {OverviewSerializer} from './serializers/overview-serializer';
import { API_BASE_URL } from '../../constants';
import { Overview } from '../../models/overview';
import { SortInterface } from '../../util/SortInterface';
import { IOverviewPage } from '../../models/overview-page';
import { SecurityGroup } from '../../models/security-group';

@Injectable({
  providedIn: 'root'
})
export class OverviewService extends ResourceService<Overview> {

  constructor(private http: HttpClient) {
      super(http, 'overview', new OverviewSerializer());
  }

  public getData(overview: Overview, page: number, filters?: any, sort?: SortInterface): Observable<IOverviewPage> {
    let filterString = '';
    if (filters) {
      Object.keys(filters).forEach(key =>
        filterString += `&${key}=${filters[key]}`
      );
    }

    let sortString = '';
    if (sort && sort.column) {
      sortString += `&sortBy=${sort.column.queryColumn}&sortOrder=${sort.reverse ? 'DESC' : 'ASC'}`;
    }

    return this.http.get<IOverviewPage>(
      `${API_BASE_URL}/${this.endpoint}/${overview.id}/data?page=${page}` + encodeURI(filterString) + sortString
    );
  }

  public updateColumns(overview: Overview): Observable<Overview> {
    return this.http.put<Overview>(`${API_BASE_URL}/${this.endpoint}/${overview.id}/columns`, overview.columns);
  }

  public getColumnFilterValues(columnId: number): Observable<string[]> {
    return this.http.get<string[]>(`${API_BASE_URL}/${this.endpoint}/column/${columnId}/filterValues`);
  }

  public updateColumnPermissions(columnId: number, userGroups: SecurityGroup[]) {
    return this.http.put(`${API_BASE_URL}/${this.endpoint}/column/${columnId}/allowedUserGroups`, userGroups.map(ug => ug.id));
  }
}
