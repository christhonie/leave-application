import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaveStatus } from '../leave-status.model';

@Component({
  selector: 'jhi-leave-status-detail',
  templateUrl: './leave-status-detail.component.html',
})
export class LeaveStatusDetailComponent implements OnInit {
  leaveStatus: ILeaveStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveStatus }) => {
      this.leaveStatus = leaveStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
