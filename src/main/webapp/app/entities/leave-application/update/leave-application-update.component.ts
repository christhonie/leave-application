import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILeaveApplication, LeaveApplication } from '../leave-application.model';
import { LeaveApplicationService } from '../service/leave-application.service';
import { ILeaveType } from 'app/entities/leave-type/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/service/leave-type.service';
import { ILeaveStatus } from 'app/entities/leave-status/leave-status.model';
import { LeaveStatusService } from 'app/entities/leave-status/service/leave-status.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

@Component({
  selector: 'jhi-leave-application-update',
  templateUrl: './leave-application-update.component.html',
})
export class LeaveApplicationUpdateComponent implements OnInit {
  isSaving = false;

  leaveTypesSharedCollection: ILeaveType[] = [];
  leaveStatusesSharedCollection: ILeaveStatus[] = [];
  staffSharedCollection: IStaff[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    appliedDate: [null, [Validators.required]],
    updateDate: [],
    days: [null, [Validators.required]],
    deleted: [null, [Validators.required]],
    leaveType: [null, Validators.required],
    leaveStatus: [],
    staff: [null, Validators.required],
  });

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    protected leaveTypeService: LeaveTypeService,
    protected leaveStatusService: LeaveStatusService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveApplication }) => {
      if (leaveApplication.id === undefined) {
        const today = dayjs().startOf('day');
        leaveApplication.appliedDate = today;
        leaveApplication.updateDate = today;
      }

      this.updateForm(leaveApplication);

      this.loadRelationshipsOptions();
    });

    this.onChanges();
  }

  onChanges(): void {
    this.editForm.get('startDate')?.valueChanges.subscribe(val => {
      const endDate = this.editForm.get(['endDate'])!.value;
      this.editForm.get(['days'])!.setValue(val && endDate ? +endDate.diff(val, 'day') + 1 : 0);
    });
    this.editForm.get('endDate')?.valueChanges.subscribe(val => {
      const startDate = this.editForm.get(['startDate'])!.value;
      this.editForm.get(['days'])!.setValue(startDate && val ? +val.diff(startDate, 'day') + 1 : 0);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveApplication = this.createFromForm();
    if (leaveApplication.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveApplicationService.update(leaveApplication));
    } else {
      this.subscribeToSaveResponse(this.leaveApplicationService.create(leaveApplication));
    }
  }

  trackLeaveTypeById(index: number, item: ILeaveType): number {
    return item.id!;
  }

  trackLeaveStatusById(index: number, item: ILeaveStatus): number {
    return item.id!;
  }

  trackStaffById(index: number, item: IStaff): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveApplication>>): void {
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

  protected updateForm(leaveApplication: ILeaveApplication): void {
    this.editForm.patchValue({
      id: leaveApplication.id,
      startDate: leaveApplication.startDate,
      endDate: leaveApplication.endDate,
      appliedDate: leaveApplication.appliedDate ? leaveApplication.appliedDate.format(DATE_TIME_FORMAT) : null,
      updateDate: leaveApplication.updateDate ? leaveApplication.updateDate.format(DATE_TIME_FORMAT) : null,
      days: leaveApplication.days,
      deleted: leaveApplication.deleted,
      leaveType: leaveApplication.leaveType,
      leaveStatus: leaveApplication.leaveStatus,
      staff: leaveApplication.staff,
    });

    this.leaveTypesSharedCollection = this.leaveTypeService.addLeaveTypeToCollectionIfMissing(
      this.leaveTypesSharedCollection,
      leaveApplication.leaveType
    );
    this.leaveStatusesSharedCollection = this.leaveStatusService.addLeaveStatusToCollectionIfMissing(
      this.leaveStatusesSharedCollection,
      leaveApplication.leaveStatus
    );
    this.staffSharedCollection = this.staffService.addStaffToCollectionIfMissing(this.staffSharedCollection, leaveApplication.staff);
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

    this.leaveStatusService
      .query()
      .pipe(map((res: HttpResponse<ILeaveStatus[]>) => res.body ?? []))
      .pipe(
        map((leaveStatuses: ILeaveStatus[]) =>
          this.leaveStatusService.addLeaveStatusToCollectionIfMissing(leaveStatuses, this.editForm.get('leaveStatus')!.value)
        )
      )
      .subscribe((leaveStatuses: ILeaveStatus[]) => (this.leaveStatusesSharedCollection = leaveStatuses));

    this.staffService
      .query()
      .pipe(map((res: HttpResponse<IStaff[]>) => res.body ?? []))
      .pipe(map((staff: IStaff[]) => this.staffService.addStaffToCollectionIfMissing(staff, this.editForm.get('staff')!.value)))
      .subscribe((staff: IStaff[]) => (this.staffSharedCollection = staff));
  }

  protected createFromForm(): ILeaveApplication {
    return {
      ...new LeaveApplication(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      appliedDate: this.editForm.get(['appliedDate'])!.value
        ? dayjs(this.editForm.get(['appliedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      days: this.editForm.get(['days'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      leaveType: this.editForm.get(['leaveType'])!.value,
      leaveStatus: this.editForm.get(['leaveStatus'])!.value,
      staff: this.editForm.get(['staff'])!.value,
    };
  }
}
