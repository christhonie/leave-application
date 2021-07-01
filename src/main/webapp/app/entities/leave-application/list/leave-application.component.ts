import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeaveApplication } from '../leave-application.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LeaveApplicationService } from '../service/leave-application.service';
import { LeaveApplicationDeleteDialogComponent } from '../delete/leave-application-delete-dialog.component';

@Component({
  selector: 'jhi-leave-application',
  templateUrl: './leave-application.component.html',
})
export class LeaveApplicationComponent implements OnInit {
  leaveApplications?: ILeaveApplication[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  newLeaveApp?: ILeaveApplication | undefined;

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.leaveApplicationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaveApplication[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: ILeaveApplication): number {
    return item.id!;
  }

  delete(leaveApplication: ILeaveApplication): void {
    const modalRef = this.modalService.open(LeaveApplicationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leaveApplication = leaveApplication;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  resubmitLeave(leaveAppId: any): void {
    this.leaveApplicationService.resubmit(leaveAppId).subscribe((res: HttpResponse<ILeaveApplication>) => {this.newLeaveApp = res.body ?? undefined;});
    let outLeaveApp: any;
    // eslint-disable-next-line prefer-const
    outLeaveApp = this.leaveApplications?.shift;
    this.router.navigate(['/leave-application', 'new'], { queryParams: { resubLeaveApp: outLeaveApp } });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'startDate') {
      result.push('startDate,asc');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ILeaveApplication[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/leave-application'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.leaveApplications = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
