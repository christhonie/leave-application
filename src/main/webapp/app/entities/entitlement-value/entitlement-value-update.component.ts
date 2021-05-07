import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IEntitlementValue, EntitlementValue } from 'app/shared/model/entitlement-value.model';
import { EntitlementValueService } from './entitlement-value.service';
import { ILeaveEntitlement } from 'app/shared/model/leave-entitlement.model';
import { LeaveEntitlementService } from 'app/entities/leave-entitlement/leave-entitlement.service';
import { IStaff } from 'app/shared/model/staff.model';
import { StaffService } from 'app/entities/staff/staff.service';

type SelectableEntity = ILeaveEntitlement | IStaff;

@Component({
  selector: 'jhi-entitlement-value-update',
  templateUrl: './entitlement-value-update.component.html',
})
export class EntitlementValueUpdateComponent implements OnInit {
  isSaving = false;
  entitlements: ILeaveEntitlement[] = [];
  staff: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    entitlementValue: [null, [Validators.required]],
    entitlementId: [null, Validators.required],
    staffId: [null, Validators.required],
  });

  constructor(
    protected entitlementValueService: EntitlementValueService,
    protected leaveEntitlementService: LeaveEntitlementService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entitlementValue }) => {
      this.updateForm(entitlementValue);

      this.leaveEntitlementService
        .query({ 'entitlementValueId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ILeaveEntitlement[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ILeaveEntitlement[]) => {
          if (!entitlementValue.entitlementId) {
            this.entitlements = resBody;
          } else {
            this.leaveEntitlementService
              .find(entitlementValue.entitlementId)
              .pipe(
                map((subRes: HttpResponse<ILeaveEntitlement>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILeaveEntitlement[]) => (this.entitlements = concatRes));
          }
        });

      this.staffService
        .query({ 'entitlementValueId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IStaff[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IStaff[]) => {
          if (!entitlementValue.staffId) {
            this.staff = resBody;
          } else {
            this.staffService
              .find(entitlementValue.staffId)
              .pipe(
                map((subRes: HttpResponse<IStaff>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IStaff[]) => (this.staff = concatRes));
          }
        });
    });
  }

  updateForm(entitlementValue: IEntitlementValue): void {
    this.editForm.patchValue({
      id: entitlementValue.id,
      entitlementValue: entitlementValue.entitlementValue,
      entitlementId: entitlementValue.entitlementId,
      staffId: entitlementValue.staffId,
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

  private createFromForm(): IEntitlementValue {
    return {
      ...new EntitlementValue(),
      id: this.editForm.get(['id'])!.value,
      entitlementValue: this.editForm.get(['entitlementValue'])!.value,
      entitlementId: this.editForm.get(['entitlementId'])!.value,
      staffId: this.editForm.get(['staffId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntitlementValue>>): void {
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
