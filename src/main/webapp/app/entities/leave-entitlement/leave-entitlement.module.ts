import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { LeaveEntitlementComponent } from './leave-entitlement.component';
import { LeaveEntitlementDetailComponent } from './leave-entitlement-detail.component';
import { LeaveEntitlementUpdateComponent } from './leave-entitlement-update.component';
import { LeaveEntitlementDeleteDialogComponent } from './leave-entitlement-delete-dialog.component';
import { leaveEntitlementRoute } from './leave-entitlement.route';

@NgModule({
  imports: [LeaveApplicationSharedModule, RouterModule.forChild(leaveEntitlementRoute)],
  declarations: [
    LeaveEntitlementComponent,
    LeaveEntitlementDetailComponent,
    LeaveEntitlementUpdateComponent,
    LeaveEntitlementDeleteDialogComponent,
  ],
  entryComponents: [LeaveEntitlementDeleteDialogComponent],
})
export class LeaveApplicationLeaveEntitlementModule {}
