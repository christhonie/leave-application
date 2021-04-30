import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntitlementValue } from 'app/shared/model/entitlement-value.model';
import { EntitlementValueService } from './entitlement-value.service';

@Component({
  templateUrl: './entitlement-value-delete-dialog.component.html',
})
export class EntitlementValueDeleteDialogComponent {
  entitlementValue?: IEntitlementValue;

  constructor(
    protected entitlementValueService: EntitlementValueService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entitlementValueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('entitlementValueListModification');
      this.activeModal.close();
    });
  }
}
