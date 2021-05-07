import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEntitlementValue, EntitlementValue } from '../entitlement-value.model';
import { EntitlementValueService } from '../service/entitlement-value.service';
import { ILeaveEntitlement } from 'app/entities/leave-entitlement/leave-entitlement.model';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/service/leave-entitlement.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

@Component({
  selector: 'jhi-entitlement-value-update',
  templateUrl: './entitlement-value-update.component.html',
})
export class EntitlementValueUpdateComponent implements OnInit {
  isSaving = false;

  entitlementsCollection: ILeaveEntitlement[] = [];
  staffCollection: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    entitlementValue: [null, [Validators.required]],
    entitlement: [null, Validators.required],
    staff: [null, Validators.required],
  });

  constructor(
    protected entitlementValueService: EntitlementValueService,
    protected leaveEntitlementService: LeaveEntitlementService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entitlementValue }) => {
      this.updateForm(entitlementValue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entitlementValue = this.createFromForm();
    if (entitlementValue.id !== undefined) {
      this.subscribeToSaveResponse(this.entitlementValueService.update(entitlementValue));
    } else {
      this.subscribeToSaveResponse(this.entitlementValueService.create(entitlementValue));
    }
  }

  trackLeaveEntitlementById(index: number, item: ILeaveEntitlement): number {
    return item.id!;
  }

  trackStaffById(index: number, item: IStaff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntitlementValue>>): void {
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

  protected updateForm(entitlementValue: IEntitlementValue): void {
    this.editForm.patchValue({
      id: entitlementValue.id,
      entitlementValue: entitlementValue.entitlementValue,
      entitlement: entitlementValue.entitlement,
      staff: entitlementValue.staff,
    });

    this.entitlementsCollection = this.leaveEntitlementService.addLeaveEntitlementToCollectionIfMissing(
      this.entitlementsCollection,
      entitlementValue.entitlement
    );
    this.staffCollection = this.staffService.addStaffToCollectionIfMissing(this.staffCollection, entitlementValue.staff);
  }

  protected loadRelationshipsOptions(): void {
    this.leaveEntitlementService
      .query({ 'entitlementValueId.specified': 'false' })
      .pipe(map((res: HttpResponse<ILeaveEntitlement[]>) => res.body ?? []))
      .pipe(
        map((leaveEntitlements: ILeaveEntitlement[]) =>
          this.leaveEntitlementService.addLeaveEntitlementToCollectionIfMissing(leaveEntitlements, this.editForm.get('entitlement')!.value)
        )
      )
      .subscribe((leaveEntitlements: ILeaveEntitlement[]) => (this.entitlementsCollection = leaveEntitlements));

    this.staffService
      .query({ 'entitlementValueId.specified': 'false' })
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing(staff, this.editForm.get('staff')!.value)))
      .subscribe((staff: IStaff[]) => (this.staffCollection = staff));
  }

  protected createFromForm(): IEntitlementValue {
    return {
      ...new EntitlementValue(),
      id: this.editForm.get(['id'])!.value,
      entitlementValue: this.editForm.get(['entitlementValue'])!.value,
      entitlement: this.editForm.get(['entitlement'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
