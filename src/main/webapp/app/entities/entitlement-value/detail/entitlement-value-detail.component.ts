import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntitlementValue } from '../entitlement-value.model';

@Component({
  selector: 'jhi-entitlement-value-detail',
  templateUrl: './entitlement-value-detail.component.html',
})
export class EntitlementValueDetailComponent implements OnInit {
  entitlementValue: IEntitlementValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entitlementValue }) => {
      this.entitlementValue = entitlementValue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
