import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDecisions, Decisions } from 'app/shared/model/decisions.model';
import { DecisionsService } from './decisions.service';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment/comment.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ILeaveApplication } from 'app/shared/model/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/leave-application.service';

type SelectableEntity = IComment | IUser | ILeaveApplication;

@Component({
  selector: 'jhi-decisions-update',
  templateUrl: './decisions-update.component.html',
})
export class DecisionsUpdateComponent implements OnInit {
  isSaving = false;
  comments: IComment[] = [];
  users: IUser[] = [];
  leaveapplications: ILeaveApplication[] = [];

  editForm = this.fb.group({
    id: [],
    choice: [null, [Validators.required]],
    decidedOn: [null, [Validators.required]],
    commentId: [],
    userId: [null, Validators.required],
    leaveApplicationId: [null, Validators.required],
  });

  constructor(
    protected decisionsService: DecisionsService,
    protected commentService: CommentService,
    protected userService: UserService,
    protected leaveApplicationService: LeaveApplicationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decisions }) => {
      if (!decisions.id) {
        const today = moment().startOf('day');
        decisions.decidedOn = today;
      }

      this.updateForm(decisions);

      this.commentService
        .query({ 'decisionsId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IComment[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IComment[]) => {
          if (!decisions.commentId) {
            this.comments = resBody;
          } else {
            this.commentService
              .find(decisions.commentId)
              .pipe(
                map((subRes: HttpResponse<IComment>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IComment[]) => (this.comments = concatRes));
          }
        });

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.leaveApplicationService.query().subscribe((res: HttpResponse<ILeaveApplication[]>) => (this.leaveapplications = res.body || []));
    });
  }

  updateForm(decisions: IDecisions): void {
    this.editForm.patchValue({
      id: decisions.id,
      choice: decisions.choice,
      decidedOn: decisions.decidedOn ? decisions.decidedOn.format(DATE_TIME_FORMAT) : null,
      commentId: decisions.commentId,
      userId: decisions.userId,
      leaveApplicationId: decisions.leaveApplicationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decisions = this.createFromForm();
    if (decisions.id !== undefined) {
      this.subscribeToSaveResponse(this.decisionsService.update(decisions));
    } else {
      this.subscribeToSaveResponse(this.decisionsService.create(decisions));
    }
  }

  private createFromForm(): IDecisions {
    return {
      ...new Decisions(),
      id: this.editForm.get(['id'])!.value,
      choice: this.editForm.get(['choice'])!.value,
      decidedOn: this.editForm.get(['decidedOn'])!.value ? moment(this.editForm.get(['decidedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      commentId: this.editForm.get(['commentId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      leaveApplicationId: this.editForm.get(['leaveApplicationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecisions>>): void {
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
