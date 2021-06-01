import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDecision } from '../decision.model';
import { DecisionService } from '../service/decision.service';

@Component({
  templateUrl: './decision-delete-dialog.component.html',
})
export class DecisionDeleteDialogComponent {
  decision?: IDecision;

  constructor(protected decisionService: DecisionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decisionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
