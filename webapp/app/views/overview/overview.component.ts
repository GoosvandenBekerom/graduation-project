import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {map, switchMap, tap} from 'rxjs/operators';
import {ClrDatagridFilterInterface, ClrDatagridSortOrder, ClrDatagridStateInterface} from '@clr/angular';
import {SnotifyService} from 'ng-snotify';
import { Overview } from '../../core/models/overview';
import { OverviewPage } from '../../core/models/overview-page';
import { JdbcType } from '../../core/models/overview-column';
import { OverviewService } from '../../core/services/resources/overview.service';
import { DevGuard } from '../../core/guards/dev.guard';
import { DevService } from '../../core/services/util/dev.service';
import { TimingService } from '../../core/services/util/timing.service';
import { PATH_ROUTE_OVERVIEW } from '../../core/constants';
import { SortInterface } from '../../core/util/SortInterface';
import { VarcharFilterComponent } from '../../shared/filters/varchar-filter/varchar-filter.component';
import { NumericFilterComponent } from '../../shared/filters/numeric-filter/numeric-filter.component';
import { BooleanFilterComponent } from '../../shared/filters/boolean-filter/boolean-filter.component';
import { DropdownFilterComponent } from '../../shared/filters/dropdown-filter/dropdown-filter.component';
import { FilterStructure, ComparisonType } from '../../core/util/FilterStructure';
import { handleRestError } from '../../globals';
import { UserGroup } from '../../core/models/user-group';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  overview: Overview;
  page: OverviewPage;

  pageNumber: number;

  loading: boolean;

  managePermissionsModalActive: boolean;
  editColumnsModalActive: boolean;

  ascOrder = ClrDatagridSortOrder.ASC;
  descOrder = ClrDatagridSortOrder.DESC;
  unsortedOrder = ClrDatagridSortOrder.UNSORTED;

  types = JdbcType;

  constructor(
    private route: ActivatedRoute,
    private service: OverviewService,
    private router: Router,
    private notify: SnotifyService,
    public devGuard: DevGuard,
    private devService: DevService,
    private time: TimingService,
  ) {}

  ngOnInit() {
    this.route.params.subscribe(({id}) => {
      if (this.overview && this.overview.id !== id) {
        // Redirect via other component so the data grid is forced to re-initialize
        this.router.navigateByUrl('/')
          .then(() => this.router.navigate([PATH_ROUTE_OVERVIEW, id]));
      } else {
        this.initializeOverview(id);
      }
    });
  }

  initializeOverview(overviewId: number) {
    this.loading = true;
    this.pageNumber = 1;

    this.time.start('initializeOverview');

    this.service.get(overviewId).pipe(
      tap(o => o.columns.sort((ca, cb) => ca.sequenceIndex - cb.sequenceIndex)),
      tap(o => this.overview = o),
      switchMap(overview => this.service.getData(overview, this.pageNumber)),
      map(p => new OverviewPage(p.content, p.page, p.size, p.totalCount))
    ).subscribe(page => {
      this.page = page;
      this.loading = false;
      this.devService.log(`overview ${this.overview.name} initialized in ${this.time.end('initializeOverview')}.`);
    }, () => this.router.navigate(['/notFound']));
  }

  refresh(state: ClrDatagridStateInterface) {
    if (!this.overview) { return; }

    this.loading = true;
    this.time.start('refresh');

    let sort: SortInterface;
    if (state.sort) {
      // THIS CURRENTLY ONLY SUPPORTS SINGLE COLUMN SORT. MIGHT CHANGE IN THE FUTURE.
      const col = this.overview.columns.find(c => c.name ? c.name === state.sort.by : c.queryColumn === state.sort.by);
      sort = new class implements SortInterface {
        column = col;
        reverse = state.sort.reverse;
      };
    }

    let filters;
    if (state.filters) {
      filters = {};
      state.filters.forEach((f: ({ property: string; value: string; } | ClrDatagridFilterInterface<any>)) => {

        // This should be the standard implementation for every filter that implements CustomFilterProperties
        // These instanceof checks make it type-safe
        if ( f instanceof VarcharFilterComponent
          || f instanceof NumericFilterComponent
          || f instanceof BooleanFilterComponent
          || f instanceof DropdownFilterComponent
        ) {
          const col = this.overview.columns.find(c => c.name ? c.name === f.property : c.queryColumn === f.property);
          filters[col.queryColumn] = JSON.stringify(f.value);
        } else {
          const defaultF = <{property: string; value: string; }> f;
          const col = this.overview.columns.find(c => c.name ? c.name === defaultF.property : c.queryColumn === defaultF.property);
          filters[col.queryColumn] = JSON.stringify(new FilterStructure(ComparisonType.AND, [{type: '=', value: defaultF.value}]));
        }
      });
    }

    this.service.getData(this.overview, this.pageNumber, filters, sort).pipe(
      map(p => new OverviewPage(p.content, p.page, p.size, p.totalCount))
    ).subscribe(page => {
      this.page = page;
      this.loading = false;
      this.devService.log(`overview ${this.overview.name} refreshed page ${page.page} in ${this.time.end('refresh')}.`);
    }, err => handleRestError(err, this.notify));
  }

  onSubmitManagePermissions(userGroups: UserGroup[]) {
    this.service.updatePermissions(this.overview, userGroups).subscribe(updatedOverview => {
      this.overview.securityGroups = updatedOverview.securityGroups;
    });
  }
}
