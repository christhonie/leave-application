import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { EntitlementValueComponent } from './entitlement-value.component';
import { EntitlementValueDetailComponent } from './entitlement-value-detail.component';
import { EntitlementValueUpdateComponent } from './entitlement-value-update.component';
import { EntitlementValueDeleteDialogComponent } from './entitlement-value-delete-dialog.component';
import { entitlementValueRoute } from './entitlement-value.route';

@NgModule({
  imports: [LeaveApplicationSharedModule, RouterModule.forChild(entitlementValueRoute)],
  declarations: [
    EntitlementValueComponent,
    EntitlementValueDetailComponent,
    EntitlementValueUpdateComponent,
    EntitlementValueDeleteDialogComponent,
  ],
  entryComponents: [EntitlementValueDeleteDialogComponent],
})
export class LeaveApplicationEntitlementValueModule {}
