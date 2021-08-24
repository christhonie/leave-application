import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILeaveType, LeaveType } from '../leave-type.model';
import { LeaveTypeService } from '../service/leave-type.service';

@Component({
  selector: 'jhi-leave-type-update',
  templateUrl: './leave-type-update.component.html',
})
export class LeaveTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    description: [null, [Validators.maxLength(200)]],
    processName: [null, [Validators.maxLength(200)]],
    dashboardOrder: [],
  });

  constructor(protected leaveTypeService: LeaveTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveType }) => {
      this.updateForm(leaveType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveType = this.createFromForm();
    if (leaveType.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveTypeService.update(leaveType));
    } else {
      this.subscribeToSaveResponse(this.leaveTypeService.create(leaveType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveType>>): void {
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

  protected updateForm(leaveType: ILeaveType): void {
    this.editForm.patchValue({
      id: leaveType.id,
      name: leaveType.name,
      description: leaveType.description,
      processName: leaveType.processName,
      dashboardOrder: leaveType.dashboardOrder,
    });
  }

  protected createFromForm(): ILeaveType {
    return {
      ...new LeaveType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      processName: this.editForm.get(['processName'])!.value,
      dashboardOrder: this.editForm.get(['dashboardOrder'])!.value,
    };
  }
}
