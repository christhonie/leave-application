import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEntitlementValue, EntitlementValue } from 'app/shared/model/entitlement-value.model';
import { EntitlementValueService } from './entitlement-value.service';
import { EntitlementValueComponent } from './entitlement-value.component';
import { EntitlementValueDetailComponent } from './entitlement-value-detail.component';
import { EntitlementValueUpdateComponent } from './entitlement-value-update.component';

@Injectable({ providedIn: 'root' })
export class EntitlementValueResolve implements Resolve<IEntitlementValue> {
  constructor(private service: EntitlementValueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntitlementValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((entitlementValue: HttpResponse<EntitlementValue>) => {
          if (entitlementValue.body) {
            return of(entitlementValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EntitlementValue());
  }
}

export const entitlementValueRoute: Routes = [
  {
    path: '',
    component: EntitlementValueComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'leaveApplicationApp.entitlementValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntitlementValueDetailComponent,
    resolve: {
      entitlementValue: EntitlementValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.entitlementValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntitlementValueUpdateComponent,
    resolve: {
      entitlementValue: EntitlementValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.entitlementValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntitlementValueUpdateComponent,
    resolve: {
      entitlementValue: EntitlementValueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'leaveApplicationApp.entitlementValue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
