import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveTypeComponent } from '../list/leave-type.component';
import { LeaveTypeDetailComponent } from '../detail/leave-type-detail.component';
import { LeaveTypeUpdateComponent } from '../update/leave-type-update.component';
import { LeaveTypeRoutingResolveService } from './leave-type-routing-resolve.service';

const leaveTypeRoute: Routes = [
  {
    path: '',
    component: LeaveTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveTypeDetailComponent,
    resolve: {
      leaveType: LeaveTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveTypeUpdateComponent,
    resolve: {
      leaveType: LeaveTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveTypeUpdateComponent,
    resolve: {
      leaveType: LeaveTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveTypeRoute)],
  exports: [RouterModule],
})
export class LeaveTypeRoutingModule {}
