import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecision, Decision } from '../decision.model';
import { DecisionService } from '../service/decision.service';

@Injectable({ providedIn: 'root' })
export class DecisionRoutingResolveService implements Resolve<IDecision> {
  constructor(protected service: DecisionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDecision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((decision: HttpResponse<Decision>) => {
          if (decision.body) {
            return of(decision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Decision());
  }
}
