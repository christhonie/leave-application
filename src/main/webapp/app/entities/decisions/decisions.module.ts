import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { DecisionsComponent } from './decisions.component';
import { DecisionsDetailComponent } from './decisions-detail.component';
import { DecisionsUpdateComponent } from './decisions-update.component';
import { DecisionsDeleteDialogComponent } from './decisions-delete-dialog.component';
import { decisionsRoute } from './decisions.route';

@NgModule({
  imports: [LeaveApplicationSharedModule, RouterModule.forChild(decisionsRoute)],
  declarations: [DecisionsComponent, DecisionsDetailComponent, DecisionsUpdateComponent, DecisionsDeleteDialogComponent],
  entryComponents: [DecisionsDeleteDialogComponent],
})
export class LeaveApplicationDecisionsModule {}
