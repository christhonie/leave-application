import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDecision } from '../decision.model';

@Component({
  selector: 'jhi-decision-detail',
  templateUrl: './decision-detail.component.html',
})
export class DecisionDetailComponent implements OnInit {
  decision: IDecision | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decision }) => {
      this.decision = decision;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
