import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveApplication, LeaveApplication } from '../leave-application.model';
import { LeaveApplicationService } from '../service/leave-application.service';

@Injectable({ providedIn: 'root' })
export class LeaveApplicationRoutingResolveService implements Resolve<ILeaveApplication> {
  constructor(protected service: LeaveApplicationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveApplication> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaveApplication: HttpResponse<LeaveApplication>) => {
          if (leaveApplication.body) {
            return of(leaveApplication.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaveApplication());
  }
}
