import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveStatusComponent } from '../list/leave-status.component';
import { LeaveStatusDetailComponent } from '../detail/leave-status-detail.component';
import { LeaveStatusUpdateComponent } from '../update/leave-status-update.component';
import { LeaveStatusRoutingResolveService } from './leave-status-routing-resolve.service';

const leaveStatusRoute: Routes = [
  {
    path: '',
    component: LeaveStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveStatusDetailComponent,
    resolve: {
      leaveStatus: LeaveStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveStatusUpdateComponent,
    resolve: {
      leaveStatus: LeaveStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveStatusUpdateComponent,
    resolve: {
      leaveStatus: LeaveStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveStatusRoute)],
  exports: [RouterModule],
})
export class LeaveStatusRoutingModule {}
