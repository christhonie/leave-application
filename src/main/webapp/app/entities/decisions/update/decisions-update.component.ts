import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDecisions, Decisions } from '../decisions.model';
import { DecisionsService } from '../service/decisions.service';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

@Component({
  selector: 'jhi-decisions-update',
  templateUrl: './decisions-update.component.html',
})
export class DecisionsUpdateComponent implements OnInit {
  isSaving = false;

  commentsCollection: IComment[] = [];
  usersSharedCollection: IUser[] = [];
  leaveApplicationsSharedCollection: ILeaveApplication[] = [];

  editForm = this.fb.group({
    id: [],
    choice: [null, [Validators.required]],
    decidedOn: [null, [Validators.required]],
    comment: [],
    user: [null, Validators.required],
    leaveApplication: [null, Validators.required],
  });

  constructor(
    protected decisionsService: DecisionsService,
    protected commentService: CommentService,
    protected userService: UserService,
    protected leaveApplicationService: LeaveApplicationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decisions }) => {
      if (decisions.id === undefined) {
        const today = dayjs().startOf('day');
        decisions.decidedOn = today;
      }

      this.updateForm(decisions);

      this.loadRelationshipsOptions();
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

  trackCommentById(index: number, item: IComment): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackLeaveApplicationById(index: number, item: ILeaveApplication): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecisions>>): void {
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

  protected updateForm(decisions: IDecisions): void {
    this.editForm.patchValue({
      id: decisions.id,
      choice: decisions.choice,
      decidedOn: decisions.decidedOn ? decisions.decidedOn.format(DATE_TIME_FORMAT) : null,
      comment: decisions.comment,
      user: decisions.user,
      leaveApplication: decisions.leaveApplication,
    });

    this.commentsCollection = this.commentService.addCommentToCollectionIfMissing(this.commentsCollection, decisions.comment);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, decisions.user);
    this.leaveApplicationsSharedCollection = this.leaveApplicationService.addLeaveApplicationToCollectionIfMissing(
      this.leaveApplicationsSharedCollection,
      decisions.leaveApplication
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commentService
      .query({ 'decisionsId.specified': 'false' })
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

  protected createFromForm(): IDecisions {
    return {
      ...new Decisions(),
      id: this.editForm.get(['id'])!.value,
      choice: this.editForm.get(['choice'])!.value,
      decidedOn: this.editForm.get(['decidedOn'])!.value ? dayjs(this.editForm.get(['decidedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      comment: this.editForm.get(['comment'])!.value,
      user: this.editForm.get(['user'])!.value,
      leaveApplication: this.editForm.get(['leaveApplication'])!.value,
    };
  }
}
