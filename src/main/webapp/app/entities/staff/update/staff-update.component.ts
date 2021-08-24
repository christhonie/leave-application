import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStaff, Staff } from '../staff.model';
import { StaffService } from '../service/staff.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

@Component({
  selector: 'jhi-staff-update',
  templateUrl: './staff-update.component.html',
})
export class StaffUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  teamsSharedCollection: ITeam[] = [];

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
    annualLeaveEntitlement: [],
    user: [],
    teams: [],
  });

  constructor(
    protected staffService: StaffService,
    protected userService: UserService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staff }) => {
      this.updateForm(staff);

      this.loadRelationshipsOptions();
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

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  getSelectedTeam(option: ITeam, selectedVals?: ITeam[]): ITeam {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaff>>): void {
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

  protected updateForm(staff: IStaff): void {
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
      annualLeaveEntitlement: staff.annualLeaveEntitlement,
      user: staff.user,
      teams: staff.teams,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, staff.user);
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, ...(staff.teams ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, ...(this.editForm.get('teams')!.value ?? []))))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): IStaff {
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
      annualLeaveEntitlement: this.editForm.get(['annualLeaveEntitlement'])!.value,
      user: this.editForm.get(['user'])!.value,
      teams: this.editForm.get(['teams'])!.value,
    };
  }
}
