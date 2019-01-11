import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {map, shareReplay, tap} from 'rxjs/operators';
import {AUTHORIZATION_TOKEN_PREFIX, BASE_URL, HEADER_AUTHORIZATION, KEY_AUTH_TOKEN, KEY_AUTH_USER} from '../constants';
import {BehaviorSubject, Observable} from 'rxjs';
import * as jwt_decode_ from 'jwt-decode';

const jwt_decode = jwt_decode_;

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedIn = new BehaviorSubject<boolean>(!!localStorage.getItem(KEY_AUTH_TOKEN));

  get isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  get loggedInUser(): any {
    return JSON.parse(localStorage.getItem(KEY_AUTH_USER)) || undefined;
  }

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<string> {
    const body = new HttpParams({fromObject: {username, password}});
    const headers = new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'});

    return this.http.post(`${BASE_URL}/login`, body.toString(), {headers: headers, observe: 'response'})
      .pipe(
        map(res => res.headers.get(HEADER_AUTHORIZATION).substr(AUTHORIZATION_TOKEN_PREFIX.length).trim()),
        tap(token => {
          if (token) {
            localStorage.setItem(KEY_AUTH_TOKEN, token);
            localStorage.setItem(KEY_AUTH_USER, JSON.stringify(jwt_decode(token)));
            this.loggedIn.next(true);
          }
        }),
        shareReplay()
      );
  }

  logout() {
    localStorage.removeItem(KEY_AUTH_TOKEN);
    localStorage.removeItem(KEY_AUTH_USER);
    this.loggedIn.next(false);
  }
}
