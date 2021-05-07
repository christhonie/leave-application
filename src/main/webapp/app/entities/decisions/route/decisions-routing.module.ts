import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DecisionsComponent } from '../list/decisions.component';
import { DecisionsDetailComponent } from '../detail/decisions-detail.component';
import { DecisionsUpdateComponent } from '../update/decisions-update.component';
import { DecisionsRoutingResolveService } from './decisions-routing-resolve.service';

const decisionsRoute: Routes = [
  {
    path: '',
    component: DecisionsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DecisionsDetailComponent,
    resolve: {
      decisions: DecisionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DecisionsUpdateComponent,
    resolve: {
      decisions: DecisionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DecisionsUpdateComponent,
    resolve: {
      decisions: DecisionsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(decisionsRoute)],
  exports: [RouterModule],
})
export class DecisionsRoutingModule {}
