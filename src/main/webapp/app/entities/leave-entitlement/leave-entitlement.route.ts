import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeaveEntitlement, LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';
import { LeaveEntitlementService } from './leave-entitlement.service';
import { LeaveEntitlementComponent } from './leave-entitlement.component';
import { LeaveEntitlementDetailComponent } from './leave-entitlement-detail.component';
import { LeaveEntitlementUpdateComponent } from './leave-entitlement-update.component';

@Injectable({ providedIn: 'root' })
export class LeaveEntitlementResolve implements Resolve<ILeaveEntitlement> {
  constructor(private service: LeaveEntitlementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveEntitlement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leaveEntitlement: HttpResponse<LeaveEntitlement>) => {
          if (leaveEntitlement.body) {
            return of(leaveEntitlement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaveEntitlement());
  }
}

export const leaveEntitlementRoute: Routes = [
  {
    path: '',
    component: LeaveEntitlementComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'leaveApplicationApp.leaveEntitlement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveEntitlementDetailComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveEntitlement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveEntitlementUpdateComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveEntitlement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveEntitlementUpdateComponent,
    resolve: {
      leaveEntitlement: LeaveEntitlementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveEntitlement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
