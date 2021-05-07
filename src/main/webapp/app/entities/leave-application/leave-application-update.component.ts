import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ILeaveApplication, LeaveApplication } from 'app/shared/model/leave-application.model';
import { LeaveApplicationService } from './leave-application.service';
import { ILeaveType } from 'app/shared/model/leave-type.model';
import { LeaveTypeService } from 'app/entities/leave-type/leave-type.service';
import { ILeaveStatus } from 'app/shared/model/leave-status.model';
import { LeaveStatusService } from 'app/entities/leave-status/leave-status.service';
import { IStaff } from 'app/shared/model/staff.model';
import { StaffService } from 'app/entities/staff/staff.service';

type SelectableEntity = ILeaveType | ILeaveStatus | IStaff;

@Component({
  selector: 'jhi-leave-application-update',
  templateUrl: './leave-application-update.component.html',
})
export class LeaveApplicationUpdateComponent implements OnInit {
  isSaving = false;
  leavetypes: ILeaveType[] = [];
  leavestatuses: ILeaveStatus[] = [];
  staff: IStaff[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    appliedDate: [null, [Validators.required]],
    updateDate: [],
    days: [null, [Validators.required]],
    deleted: [null, [Validators.required]],
    leaveTypeId: [null, Validators.required],
    leaveStatusId: [],
    staffId: [null, Validators.required],
  });

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    protected leaveTypeService: LeaveTypeService,
    protected leaveStatusService: LeaveStatusService,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveApplication }) => {
      if (!leaveApplication.id) {
        const today = moment().startOf('day');
        leaveApplication.appliedDate = today;
        leaveApplication.updateDate = today;
      }

      this.updateForm(leaveApplication);

      this.leaveTypeService.query().subscribe((res: HttpResponse<ILeaveType[]>) => (this.leavetypes = res.body || []));

      this.leaveStatusService.query().subscribe((res: HttpResponse<ILeaveStatus[]>) => (this.leavestatuses = res.body || []));

      this.staffService.query().subscribe((res: HttpResponse<IStaff[]>) => (this.staff = res.body || []));
    });
  }

  updateForm(leaveApplication: ILeaveApplication): void {
    this.editForm.patchValue({
      id: leaveApplication.id,
      startDate: leaveApplication.startDate,
      endDate: leaveApplication.endDate,
      appliedDate: leaveApplication.appliedDate ? leaveApplication.appliedDate.format(DATE_TIME_FORMAT) : null,
      updateDate: leaveApplication.updateDate ? leaveApplication.updateDate.format(DATE_TIME_FORMAT) : null,
      days: leaveApplication.days,
      deleted: leaveApplication.deleted,
      leaveTypeId: leaveApplication.leaveTypeId,
      leaveStatusId: leaveApplication.leaveStatusId,
      staffId: leaveApplication.staffId,
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

  private createFromForm(): ILeaveApplication {
    return {
      ...new LeaveApplication(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      appliedDate: this.editForm.get(['appliedDate'])!.value
        ? moment(this.editForm.get(['appliedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? moment(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      days: this.editForm.get(['days'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      leaveTypeId: this.editForm.get(['leaveTypeId'])!.value,
      leaveStatusId: this.editForm.get(['leaveStatusId'])!.value,
      staffId: this.editForm.get(['staffId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveApplication>>): void {
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
