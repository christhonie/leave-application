import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveApplicationComponent } from '../list/leave-application.component';
import { LeaveApplicationDetailComponent } from '../detail/leave-application-detail.component';
import { LeaveApplicationUpdateComponent } from '../update/leave-application-update.component';
import { LeaveApplicationRoutingResolveService } from './leave-application-routing-resolve.service';

const leaveApplicationRoute: Routes = [
  {
    path: '',
    component: LeaveApplicationComponent,
    data: {
      defaultSort: 'startDate,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveApplicationDetailComponent,
    resolve: {
      leaveApplication: LeaveApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveApplicationUpdateComponent,
    resolve: {
      leaveApplication: LeaveApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveApplicationUpdateComponent,
    resolve: {
      leaveApplication: LeaveApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveApplicationRoute)],
  exports: [RouterModule],
})
export class LeaveApplicationRoutingModule {}
