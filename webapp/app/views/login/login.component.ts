import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';
import {SnotifyService} from 'ng-snotify';
import {handleRestError} from '../../globals';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string;
  password: string;

  constructor(
    private authService: AuthService,
    private router: Router,
    private notify: SnotifyService
  ) { }

  ngOnInit() {
    this.authService.isLoggedIn.subscribe(loggedIn => {
        if (loggedIn) { this.navigateToHome(); }
    });
  }

  onLoginSubmit() {
    if (!this.username || !this.password) { return; }

    this.authService.login(this.username, this.password)
      .subscribe(
      () => this.navigateToHome(),
        _err => handleRestError('Incorrect username or password', this.notify)
      );
  }

  private navigateToHome() {
    this.router.navigate(['/']);
  }
}
