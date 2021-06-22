import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPublicHoliday } from '../public-holiday.model';
import { PublicHolidayService } from '../service/public-holiday.service';

@Component({
  templateUrl: './public-holiday-delete-dialog.component.html',
})
export class PublicHolidayDeleteDialogComponent {
  publicHoliday?: IPublicHoliday;

  constructor(protected publicHolidayService: PublicHolidayService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.publicHolidayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
