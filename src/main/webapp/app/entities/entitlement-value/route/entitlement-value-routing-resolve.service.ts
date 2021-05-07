import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEntitlementValue, EntitlementValue } from '../entitlement-value.model';
import { EntitlementValueService } from '../service/entitlement-value.service';

@Injectable({ providedIn: 'root' })
export class EntitlementValueRoutingResolveService implements Resolve<IEntitlementValue> {
  constructor(protected service: EntitlementValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntitlementValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((entitlementValue: HttpResponse<EntitlementValue>) => {
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
