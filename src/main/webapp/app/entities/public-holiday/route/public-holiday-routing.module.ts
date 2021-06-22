import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PublicHolidayComponent } from '../list/public-holiday.component';
import { PublicHolidayDetailComponent } from '../detail/public-holiday-detail.component';
import { PublicHolidayUpdateComponent } from '../update/public-holiday-update.component';
import { PublicHolidayRoutingResolveService } from './public-holiday-routing-resolve.service';

const publicHolidayRoute: Routes = [
  {
    path: '',
    component: PublicHolidayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PublicHolidayDetailComponent,
    resolve: {
      publicHoliday: PublicHolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PublicHolidayUpdateComponent,
    resolve: {
      publicHoliday: PublicHolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'reload',
    component: PublicHolidayUpdateComponent,
    resolve: {
      publicHoliday: PublicHolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PublicHolidayUpdateComponent,
    resolve: {
      publicHoliday: PublicHolidayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(publicHolidayRoute)],
  exports: [RouterModule],
})
export class PublicHolidayRoutingModule {}
