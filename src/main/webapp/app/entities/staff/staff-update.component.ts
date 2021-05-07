import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStaff, Staff } from 'app/shared/model/staff.model';
import { StaffService } from './staff.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';

type SelectableEntity = IUser | ITeam;

@Component({
  selector: 'jhi-staff-update',
  templateUrl: './staff-update.component.html',
})
export class StaffUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  teams: ITeam[] = [];
  startDateDp: any;

  editForm = this.fb.group({
    id: [],
    position: [null, [Validators.maxLength(50)]],
    employeeID: [null, [Validators.required, Validators.maxLength(50)]],
    startDate: [null, [Validators.required]],
    name: [null, [Validators.maxLength(100)]],
    firstName: [null, [Validators.required, Validators.maxLength(50)]],
    lastName: [null, [Validators.required, Validators.maxLength(50)]],
    email: [null, [Validators.maxLength(100)]],
    contractNumber: [null, [Validators.maxLength(50)]],
    gender: [null, [Validators.required, Validators.maxLength(2)]],
    userId: [],
    teams: [],
  });

  constructor(
    protected staffService: StaffService,
    protected userService: UserService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staff }) => {
      this.updateForm(staff);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.teamService.query().subscribe((res: HttpResponse<ITeam[]>) => (this.teams = res.body || []));
    });
  }

  updateForm(staff: IStaff): void {
    this.editForm.patchValue({
      id: staff.id,
      position: staff.position,
      employeeID: staff.employeeID,
      startDate: staff.startDate,
      name: staff.name,
      firstName: staff.firstName,
      lastName: staff.lastName,
      email: staff.email,
      contractNumber: staff.contractNumber,
      gender: staff.gender,
      userId: staff.userId,
      teams: staff.teams,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staff = this.createFromForm();
    if (staff.id !== undefined) {
      this.subscribeToSaveResponse(this.staffService.update(staff));
    } else {
      this.subscribeToSaveResponse(this.staffService.create(staff));
    }
  }

  private createFromForm(): IStaff {
    return {
      ...new Staff(),
      id: this.editForm.get(['id'])!.value,
      position: this.editForm.get(['position'])!.value,
      employeeID: this.editForm.get(['employeeID'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      name: this.editForm.get(['name'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      contractNumber: this.editForm.get(['contractNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      teams: this.editForm.get(['teams'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaff>>): void {
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

  getSelected(selectedVals: ITeam[], option: ITeam): ITeam {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
