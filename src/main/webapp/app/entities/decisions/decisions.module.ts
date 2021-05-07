import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DecisionsComponent } from './list/decisions.component';
import { DecisionsDetailComponent } from './detail/decisions-detail.component';
import { DecisionsUpdateComponent } from './update/decisions-update.component';
import { DecisionsDeleteDialogComponent } from './delete/decisions-delete-dialog.component';
import { DecisionsRoutingModule } from './route/decisions-routing.module';

@NgModule({
  imports: [SharedModule, DecisionsRoutingModule],
  declarations: [DecisionsComponent, DecisionsDetailComponent, DecisionsUpdateComponent, DecisionsDeleteDialogComponent],
  entryComponents: [DecisionsDeleteDialogComponent],
})
export class DecisionsModule {}
