import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import {AuthService} from 'core/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class DevGuard implements CanActivate {

  constructor(private authService: AuthService) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const user = this.authService.loggedInUser;
    return user && user.group === 'DEV';
  }
}
