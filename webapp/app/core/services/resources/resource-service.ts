import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Serializer} from './serializers/serializer';
import {API_BASE_URL} from '../../constants';
import {Resource} from '../../models/resource';
import {UserGroup} from '../../models/user-group';

export class ResourceService<T extends Resource> {
    constructor(
        private httpClient: HttpClient,
        public endpoint: string,
        private serializer: Serializer<T>
    ) {}

    public get(id: number): Observable<T> {
        return this.httpClient
            .get<T>(`${API_BASE_URL}/${this.endpoint}/${id}`);
    }

    public getAll(): Observable<T[]> {
        return this.httpClient
            .get<T[]>(`${API_BASE_URL}/${this.endpoint}`);
    }

    public create(item: T): Observable<T> {
        return this.httpClient
            .post<T>(`${API_BASE_URL}/${this.endpoint}`, this.serializer.toJson(item));
    }

    public update(item: T): Observable<T> {
        return this.httpClient
            .put<T>(`${API_BASE_URL}/${this.endpoint}/${item.id}`, this.serializer.toJson(item));
    }

    public delete(id: number) {
        return this.httpClient
            .delete(`${API_BASE_URL}/${this.endpoint}/${id}`);
    }

    public updatePermissions(item: T, userGroups: UserGroup[]) {
        return this.httpClient
          .put<T>(`${API_BASE_URL}/${this.endpoint}/${item.id}/allowedUserGroups`, userGroups.map(ug => ug.id));
    }
}
