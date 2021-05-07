import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDecisions } from 'app/shared/model/decisions.model';

@Component({
  selector: 'jhi-decisions-detail',
  templateUrl: './decisions-detail.component.html',
})
export class DecisionsDetailComponent implements OnInit {
  decisions: IDecisions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decisions }) => (this.decisions = decisions));
  }

  previousState(): void {
    window.history.back();
  }
}
