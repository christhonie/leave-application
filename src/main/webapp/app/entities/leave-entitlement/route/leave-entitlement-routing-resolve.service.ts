import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveEntitlement, LeaveEntitlement } from '../leave-entitlement.model';
import { LeaveEntitlementService } from '../service/leave-entitlement.service';

@Injectable({ providedIn: 'root' })
export class LeaveEntitlementRoutingResolveService implements Resolve<ILeaveEntitlement> {
  constructor(protected service: LeaveEntitlementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveEntitlement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaveEntitlement: HttpResponse<LeaveEntitlement>) => {
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
