import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeaveEntitlement, LeaveEntitlement } from 'app/shared/model/leave-entitlement.model';
import { LeaveEntitlementService } from './leave-entitlement.service';
import { ILeaveType } from 'app/shared/model/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/leave-type.service';
import { IStaff } from 'app/shared/model/staff.model';
import { StaffService } from 'app/entities/staff/staff.service';

type SelectableEntity = ILeaveType | IStaff;

@Component({
  selector: 'jhi-leave-entitlement-update',
  templateUrl: './leave-entitlement-update.component.html',
})
export class LeaveEntitlementUpdateComponent implements OnInit {
  isSaving = false;
  leavetypes: ILeaveType[] = [];
  staff: IStaff[] = [];
  entitlementDateDp: any;

  editForm = this.fb.group({
    id: [],
    entitlementDate: [null, [Validators.required]],
    days: [null, [Validators.required]],
    leaveTypeId: [null, Validators.required],
    staffId: [null, Validators.required],
  });

  constructor(
    protected leaveEntitlementService: LeaveEntitlementService,
    protected leaveTypeService: LeaveTypeService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveEntitlement }) => {
      this.updateForm(leaveEntitlement);

      this.leaveTypeService.query().subscribe((res: HttpResponse<ILeaveType[]>) => (this.leavetypes = res.body || []));

      this.staffService.query().subscribe((res: HttpResponse<IStaff[]>) => (this.staff = res.body || []));
    });
  }

  updateForm(leaveEntitlement: ILeaveEntitlement): void {
    this.editForm.patchValue({
      id: leaveEntitlement.id,
      entitlementDate: leaveEntitlement.entitlementDate,
      days: leaveEntitlement.days,
      leaveTypeId: leaveEntitlement.leaveTypeId,
      staffId: leaveEntitlement.staffId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveEntitlement = this.createFromForm();
    if (leaveEntitlement.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveEntitlementService.update(leaveEntitlement));
    } else {
      this.subscribeToSaveResponse(this.leaveEntitlementService.create(leaveEntitlement));
    }
  }

  private createFromForm(): ILeaveEntitlement {
    return {
      ...new LeaveEntitlement(),
      id: this.editForm.get(['id'])!.value,
      entitlementDate: this.editForm.get(['entitlementDate'])!.value,
      days: this.editForm.get(['days'])!.value,
      leaveTypeId: this.editForm.get(['leaveTypeId'])!.value,
      staffId: this.editForm.get(['staffId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveEntitlement>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
