import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {MenuItem, MenuItemService, AuthService, DevGuard, PATH_ROUTE_OVERVIEW, DEFAULT_ICON} from '../core';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent implements OnInit {

    loggedIn$: Observable<boolean>;

    menuItems$: Observable<MenuItem[]>;

    overviewPath = PATH_ROUTE_OVERVIEW;
    defaultIcon = DEFAULT_ICON;

    navCollapsed: boolean;

    constructor(
      private menuItemService: MenuItemService,
      private authService: AuthService,
      private cdr: ChangeDetectorRef,
      public devGuard: DevGuard,
    ) { }

    ngOnInit() {
        this.menuItems$ = this.menuItemService.getAll();
        this.loggedIn$ = this.authService.isLoggedIn;
    }

    collapseNavigation() {
        if (!this.navCollapsed) {
            this.navCollapsed = !this.navCollapsed;
            this.cdr.detectChanges();
        }
    }
}
