import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComment, Comment } from '../comment.model';
import { CommentService } from '../service/comment.service';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;

  leaveApplicationsSharedCollection: ILeaveApplication[] = [];

  editForm = this.fb.group({
    id: [],
    comment: [null, [Validators.required, Validators.maxLength(5000)]],
    leaveApplication: [null, Validators.required],
  });

  constructor(
    protected commentService: CommentService,
    protected leaveApplicationService: LeaveApplicationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.updateForm(comment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  trackLeaveApplicationById(index: number, item: ILeaveApplication): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
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

  protected updateForm(comment: IComment): void {
    this.editForm.patchValue({
      id: comment.id,
      comment: comment.comment,
      leaveApplication: comment.leaveApplication,
    });

    this.leaveApplicationsSharedCollection = this.leaveApplicationService.addLeaveApplicationToCollectionIfMissing(
      this.leaveApplicationsSharedCollection,
      comment.leaveApplication
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IComment {
    return {
      ...new Comment(),
      id: this.editForm.get(['id'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      leaveApplication: this.editForm.get(['leaveApplication'])!.value,
    };
  }
}
