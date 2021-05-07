import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EntitlementValueComponent } from '../list/entitlement-value.component';
import { EntitlementValueDetailComponent } from '../detail/entitlement-value-detail.component';
import { EntitlementValueUpdateComponent } from '../update/entitlement-value-update.component';
import { EntitlementValueRoutingResolveService } from './entitlement-value-routing-resolve.service';

const entitlementValueRoute: Routes = [
  {
    path: '',
    component: EntitlementValueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntitlementValueDetailComponent,
    resolve: {
      entitlementValue: EntitlementValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntitlementValueUpdateComponent,
    resolve: {
      entitlementValue: EntitlementValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntitlementValueUpdateComponent,
    resolve: {
      entitlementValue: EntitlementValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(entitlementValueRoute)],
  exports: [RouterModule],
})
export class EntitlementValueRoutingModule {}
