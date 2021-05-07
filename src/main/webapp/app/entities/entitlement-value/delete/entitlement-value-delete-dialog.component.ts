import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntitlementValue } from '../entitlement-value.model';
import { EntitlementValueService } from '../service/entitlement-value.service';

@Component({
  templateUrl: './entitlement-value-delete-dialog.component.html',
})
export class EntitlementValueDeleteDialogComponent {
  entitlementValue?: IEntitlementValue;

  constructor(protected entitlementValueService: EntitlementValueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entitlementValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
