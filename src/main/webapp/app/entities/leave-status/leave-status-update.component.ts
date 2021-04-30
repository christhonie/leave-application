import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeaveStatus, LeaveStatus } from 'app/shared/model/leave-status.model';
import { LeaveStatusService } from './leave-status.service';

@Component({
  selector: 'jhi-leave-status-update',
  templateUrl: './leave-status-update.component.html',
})
export class LeaveStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    description: [null, [Validators.maxLength(200)]],
  });

  constructor(protected leaveStatusService: LeaveStatusService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveStatus }) => {
      this.updateForm(leaveStatus);
    });
  }

  updateForm(leaveStatus: ILeaveStatus): void {
    this.editForm.patchValue({
      id: leaveStatus.id,
      name: leaveStatus.name,
      description: leaveStatus.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveStatus = this.createFromForm();
    if (leaveStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.leaveStatusService.update(leaveStatus));
    } else {
      this.subscribeToSaveResponse(this.leaveStatusService.create(leaveStatus));
    }
  }

  private createFromForm(): ILeaveStatus {
    return {
      ...new LeaveStatus(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveStatus>>): void {
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
}
