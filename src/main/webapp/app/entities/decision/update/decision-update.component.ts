import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDecision, Decision } from '../decision.model';
import { DecisionService } from '../service/decision.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

@Component({
  selector: 'jhi-decision-update',
  templateUrl: './decision-update.component.html',
})
export class DecisionUpdateComponent implements OnInit {
  isSaving = false;

  commentsCollection: IComment[] = [];
  usersSharedCollection: IUser[] = [];
  leaveApplicationsSharedCollection: ILeaveApplication[] = [];

  editForm = this.fb.group({
    id: [],
    choice: [null, [Validators.required]],
    decidedOn: [null, [Validators.required]],
    comment: [],
    user: [],
    leaveApplication: [null, Validators.required],
  });

  constructor(
    protected decisionService: DecisionService,
    protected commentService: CommentService,
    protected userService: UserService,
    protected leaveApplicationService: LeaveApplicationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decision }) => {
      if (decision.id === undefined) {
        const today = dayjs().startOf('day');
        decision.decidedOn = today;
      }

      this.updateForm(decision);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decision = this.createFromForm();
    if (decision.id !== undefined) {
      this.subscribeToSaveResponse(this.decisionService.update(decision));
    } else {
      this.subscribeToSaveResponse(this.decisionService.create(decision));
    }
  }

  trackCommentById(index: number, item: IComment): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackLeaveApplicationById(index: number, item: ILeaveApplication): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecision>>): void {
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

  protected updateForm(decision: IDecision): void {
    this.editForm.patchValue({
      id: decision.id,
      choice: decision.choice,
      decidedOn: decision.decidedOn ? decision.decidedOn.format(DATE_TIME_FORMAT) : null,
      comment: decision.comment,
      user: decision.user,
      leaveApplication: decision.leaveApplication,
    });

    this.commentsCollection = this.commentService.addCommentToCollectionIfMissing(this.commentsCollection, decision.comment);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, decision.user);
    this.leaveApplicationsSharedCollection = this.leaveApplicationService.addLeaveApplicationToCollectionIfMissing(
      this.leaveApplicationsSharedCollection,
      decision.leaveApplication
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commentService
      .query({ 'decisionId.specified': 'false' })
      .pipe(map((res: HttpResponse<IComment[]>) => res.body ?? []))
      .pipe(
        map((comments: IComment[]) => this.commentService.addCommentToCollectionIfMissing(comments, this.editForm.get('comment')!.value))
      )
      .subscribe((comments: IComment[]) => (this.commentsCollection = comments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.leaveApplicationService
      .query()
      .pipe(map((res: HttpResponse<ILeaveApplication[]>) => res.body ?? []))
      .pipe(
        map((leaveApplications: ILeaveApplication[]) =>
          this.leaveApplicationService.addLeaveApplicationToCollectionIfMissing(
            leaveApplications,
            this.editForm.get('leaveApplication')!.value
          )
        )
      )
      .subscribe((leaveApplications: ILeaveApplication[]) => (this.leaveApplicationsSharedCollection = leaveApplications));
  }

  protected createFromForm(): IDecision {
    return {
      ...new Decision(),
      id: this.editForm.get(['id'])!.value,
      choice: this.editForm.get(['choice'])!.value,
      decidedOn: this.editForm.get(['decidedOn'])!.value ? dayjs(this.editForm.get(['decidedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      comment: this.editForm.get(['comment'])!.value,
      user: this.editForm.get(['user'])!.value,
      leaveApplication: this.editForm.get(['leaveApplication'])!.value,
    };
  }
}
