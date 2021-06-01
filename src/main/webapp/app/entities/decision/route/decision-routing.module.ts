import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DecisionComponent } from '../list/decision.component';
import { DecisionDetailComponent } from '../detail/decision-detail.component';
import { DecisionUpdateComponent } from '../update/decision-update.component';
import { DecisionRoutingResolveService } from './decision-routing-resolve.service';

const decisionRoute: Routes = [
  {
    path: '',
    component: DecisionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DecisionDetailComponent,
    resolve: {
      decision: DecisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DecisionUpdateComponent,
    resolve: {
      decision: DecisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DecisionUpdateComponent,
    resolve: {
      decision: DecisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(decisionRoute)],
  exports: [RouterModule],
})
export class DecisionRoutingModule {}
