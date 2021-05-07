import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeaveStatus, LeaveStatus } from 'app/shared/model/leave-status.model';
import { LeaveStatusService } from './leave-status.service';
import { LeaveStatusComponent } from './leave-status.component';
import { LeaveStatusDetailComponent } from './leave-status-detail.component';
import { LeaveStatusUpdateComponent } from './leave-status-update.component';

@Injectable({ providedIn: 'root' })
export class LeaveStatusResolve implements Resolve<ILeaveStatus> {
  constructor(private service: LeaveStatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leaveStatus: HttpResponse<LeaveStatus>) => {
          if (leaveStatus.body) {
            return of(leaveStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaveStatus());
  }
}

export const leaveStatusRoute: Routes = [
  {
    path: '',
    component: LeaveStatusComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'leaveApplicationApp.leaveStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveStatusDetailComponent,
    resolve: {
      leaveStatus: LeaveStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveStatusUpdateComponent,
    resolve: {
      leaveStatus: LeaveStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveStatusUpdateComponent,
    resolve: {
      leaveStatus: LeaveStatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.leaveStatus.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
