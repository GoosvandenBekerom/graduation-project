import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import { MenuItem } from '../core/models/menu-item';
import { PATH_ROUTE_OVERVIEW, DEFAULT_ICON } from '../core/constants';
import { MenuItemService } from '../core/services/resources/menu-item.service';
import { AuthService } from '../core/services/auth.service';
import { DevGuard } from '../core/guards/dev.guard';

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
