import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecisions, Decisions } from '../decisions.model';
import { DecisionsService } from '../service/decisions.service';

@Injectable({ providedIn: 'root' })
export class DecisionsRoutingResolveService implements Resolve<IDecisions> {
  constructor(protected service: DecisionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDecisions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((decisions: HttpResponse<Decisions>) => {
          if (decisions.body) {
            return of(decisions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Decisions());
  }
}
