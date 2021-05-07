import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDecisions } from '../decisions.model';
import { DecisionsService } from '../service/decisions.service';

@Component({
  templateUrl: './decisions-delete-dialog.component.html',
})
export class DecisionsDeleteDialogComponent {
  decisions?: IDecisions;

  constructor(protected decisionsService: DecisionsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decisionsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
