import { HttpClientModule } from '@angular/common/http';
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExternalUrlDirective } from './directives/external-url.directive';
import { ActionService } from './services/resources/action.service';
import { MenuItemService } from './services/resources/menu-item.service';
import { OverviewService } from './services/resources/overview.service';
import { UserGroupService } from './services/resources/user-group.service';
import { DevService } from './services/util/dev.service';
import { TimingService } from './services/util/timing.service';
import { AuthService } from './services/auth.service';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
  ],
  declarations: [
    ExternalUrlDirective,
  ],
  providers: [
    ActionService,
    MenuItemService,
    OverviewService,
    UserGroupService,
    DevService,
    TimingService,
    AuthService
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() core: CoreModule) {
    if (core) {
      throw new Error('CoreModule should only be be imported in AppModule');
    }
  }
}
