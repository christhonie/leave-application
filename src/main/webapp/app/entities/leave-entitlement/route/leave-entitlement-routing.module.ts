import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveEntitlementComponent } from '../list/leave-entitlement.component';
import { LeaveEntitlementDetailComponent } from '../detail/leave-entitlement-detail.component';
import { LeaveEntitlementUpdateComponent } from '../update/leave-entitlement-update.component';
import { LeaveEntitlementRoutingResolveService } from './leave-entitlement-routing-resolve.service';

const leaveEntitlementRoute: Routes = [
  {
    path: '',
    component: LeaveEntitlementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveEntitlementDetailComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveEntitlementUpdateComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveEntitlementUpdateComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveEntitlementRoute)],
  exports: [RouterModule],
})
export class LeaveEntitlementRoutingModule {}
