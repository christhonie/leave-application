import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PublicHolidayComponent } from './list/public-holiday.component';
import { PublicHolidayDetailComponent } from './detail/public-holiday-detail.component';
import { PublicHolidayUpdateComponent } from './update/public-holiday-update.component';
import { PublicHolidayDeleteDialogComponent } from './delete/public-holiday-delete-dialog.component';
import { PublicHolidayRoutingModule } from './route/public-holiday-routing.module';

@NgModule({
  imports: [SharedModule, PublicHolidayRoutingModule],
  declarations: [PublicHolidayComponent, PublicHolidayDetailComponent, PublicHolidayUpdateComponent, PublicHolidayDeleteDialogComponent],
  entryComponents: [PublicHolidayDeleteDialogComponent],
})
export class PublicHolidayModule {}
