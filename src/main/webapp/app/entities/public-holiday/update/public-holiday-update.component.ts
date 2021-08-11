import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPublicHoliday, PublicHoliday } from '../public-holiday.model';
import { PublicHolidayService } from '../service/public-holiday.service';

@Component({
  selector: 'jhi-public-holiday-update',
  templateUrl: './public-holiday-update.component.html',
})
export class PublicHolidayUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    date: [],
  });

  constructor(protected publicHolidayService: PublicHolidayService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ publicHoliday }) => {
      this.updateForm(publicHoliday);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const publicHoliday = this.createFromForm();
    if (publicHoliday.id !== undefined) {
      this.subscribeToSaveResponse(this.publicHolidayService.update(publicHoliday));
    } else {
      this.subscribeToSaveResponse(this.publicHolidayService.create(publicHoliday));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPublicHoliday>>): void {
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

  protected updateForm(publicHoliday: IPublicHoliday): void {
    this.editForm.patchValue({
      id: publicHoliday.id,
      name: publicHoliday.name,
      date: publicHoliday.date,
    });
  }

  protected createFromForm(): IPublicHoliday {
    return {
      ...new PublicHoliday(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      date: this.editForm.get(['date'])!.value,
    };
  }
}
