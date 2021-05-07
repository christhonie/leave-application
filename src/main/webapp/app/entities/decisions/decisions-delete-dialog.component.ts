import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDecisions } from 'app/shared/model/decisions.model';
import { DecisionsService } from './decisions.service';

@Component({
  templateUrl: './decisions-delete-dialog.component.html',
})
export class DecisionsDeleteDialogComponent {
  decisions?: IDecisions;

  constructor(protected decisionsService: DecisionsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decisionsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('decisionsListModification');
      this.activeModal.close();
    });
  }
}
