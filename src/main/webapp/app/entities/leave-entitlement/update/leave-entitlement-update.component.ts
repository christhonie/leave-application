import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILeaveEntitlement, LeaveEntitlement } from '../leave-entitlement.model';
import { LeaveEntitlementService } from '../service/leave-entitlement.service';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

@Component({
  selector: 'jhi-leave-entitlement-update',
  templateUrl: './leave-entitlement-update.component.html',
})
export class LeaveEntitlementUpdateComponent implements OnInit {
  isSaving = false;

  leaveTypesSharedCollection: ILeaveType[] = [];
  staffSharedCollection: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    entitlementDate: [null, [Validators.required]],
    expiryDate: [null, [Validators.required]],
    days: [null, [Validators.required]],
    leaveType: [null, Validators.required],
    staff: [null, Validators.required],
  });

  constructor(
    protected leaveEntitlementService: LeaveEntitlementService,
    protected leaveTypeService: LeaveTypeService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveEntitlement }) => {
      this.updateForm(leaveEntitlement);

      this.loadRelationshipsOptions();
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

  trackLeaveTypeById(index: number, item: ILeaveType): number {
    return item.id!;
  }

  trackStaffById(index: number, item: IStaff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveEntitlement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(leaveEntitlement: ILeaveEntitlement): void {
    this.editForm.patchValue({
      id: leaveEntitlement.id,
      entitlementDate: leaveEntitlement.entitlementDate,
      expiryDate: leaveEntitlement.expiryDate,
      days: leaveEntitlement.days,
      leaveType: leaveEntitlement.leaveType,
      staff: leaveEntitlement.staff,
    });

    this.leaveTypesSharedCollection = this.leaveTypeService.addLeaveTypeToCollectionIfMissing(
      this.leaveTypesSharedCollection,
      leaveEntitlement.leaveType
    );
    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing(this.staffSharedCollection, leaveEntitlement.staff);
  }

  protected loadRelationshipsOptions(): void {
    this.leaveTypeService
      .query()
      .pipe(map((res: HttpResponse<ILeaveType[]>) => res.body ?? []))
      .pipe(
        map((leaveTypes: ILeaveType[]) =>
          this.leaveTypeService.addLeaveTypeToCollectionIfMissing(leaveTypes, this.editForm.get('leaveType')!.value)
        )
      )
      .subscribe((leaveTypes: ILeaveType[]) => (this.leaveTypesSharedCollection = leaveTypes));

    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing(staff, this.editForm.get('staff')!.value)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));
  }

  protected createFromForm(): ILeaveEntitlement {
    return {
      ...new LeaveEntitlement(),
      id: this.editForm.get(['id'])!.value,
      entitlementDate: this.editForm.get(['entitlementDate'])!.value,
      expiryDate: this.editForm.get(['expiryDate'])!.value,
      days: this.editForm.get(['days'])!.value,
      leaveType: this.editForm.get(['leaveType'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
