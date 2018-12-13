import { Component } from '@angular/core';
import {Observable} from 'rxjs';
import { PATH_ROUTE_EDITOR } from 'core/constants';
import { AuthService } from 'core/services/auth.service';
import { DevGuard } from 'core/guards/dev.guard';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  editorPath = PATH_ROUTE_EDITOR;
  loggedIn$: Observable<boolean>;

  constructor(public authService: AuthService, public devGuard: DevGuard) {
    this.loggedIn$ = this.authService.isLoggedIn;
  }

  onLogoutClick() {
    this.authService.logout();
  }
}
