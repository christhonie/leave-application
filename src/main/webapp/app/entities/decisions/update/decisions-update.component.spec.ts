jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DecisionsService } from '../service/decisions.service';
import { IDecisions, Decisions } from '../decisions.model';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

import { DecisionsUpdateComponent } from './decisions-update.component';

describe('Component Tests', () => {
  describe('Decisions Management Update Component', () => {
    let comp: DecisionsUpdateComponent;
    let fixture: ComponentFixture<DecisionsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let decisionsService: DecisionsService;
    let commentService: CommentService;
    let userService: UserService;
    let leaveApplicationService: LeaveApplicationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DecisionsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DecisionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DecisionsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      decisionsService = TestBed.inject(DecisionsService);
      commentService = TestBed.inject(CommentService);
      userService = TestBed.inject(UserService);
      leaveApplicationService = TestBed.inject(LeaveApplicationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call comment query and add missing value', () => {
        const decisions: IDecisions = { id: 456 };
        const comment: IComment = { id: 56195 };
        decisions.comment = comment;

        const commentCollection: IComment[] = [{ id: 7255 }];
        spyOn(commentService, 'query').and.returnValue(of(new HttpResponse({ body: commentCollection })));
        const expectedCollection: IComment[] = [comment, ...commentCollection];
        spyOn(commentService, 'addCommentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        expect(commentService.query).toHaveBeenCalled();
        expect(commentService.addCommentToCollectionIfMissing).toHaveBeenCalledWith(commentCollection, comment);
        expect(comp.commentsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const decisions: IDecisions = { id: 456 };
        const user: IUser = { id: 15999 };
        decisions.user = user;

        const userCollection: IUser[] = [{ id: 47420 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LeaveApplication query and add missing value', () => {
        const decisions: IDecisions = { id: 456 };
        const leaveApplication: ILeaveApplication = { id: 88904 };
        decisions.leaveApplication = leaveApplication;

        const leaveApplicationCollection: ILeaveApplication[] = [{ id: 15862 }];
        spyOn(leaveApplicationService, 'query').and.returnValue(of(new HttpResponse({ body: leaveApplicationCollection })));
        const additionalLeaveApplications = [leaveApplication];
        const expectedCollection: ILeaveApplication[] = [...additionalLeaveApplications, ...leaveApplicationCollection];
        spyOn(leaveApplicationService, 'addLeaveApplicationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        expect(leaveApplicationService.query).toHaveBeenCalled();
        expect(leaveApplicationService.addLeaveApplicationToCollectionIfMissing).toHaveBeenCalledWith(
          leaveApplicationCollection,
          ...additionalLeaveApplications
        );
        expect(comp.leaveApplicationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const decisions: IDecisions = { id: 456 };
        const comment: IComment = { id: 57746 };
        decisions.comment = comment;
        const user: IUser = { id: 86007 };
        decisions.user = user;
        const leaveApplication: ILeaveApplication = { id: 42769 };
        decisions.leaveApplication = leaveApplication;

        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(decisions));
        expect(comp.commentsCollection).toContain(comment);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.leaveApplicationsSharedCollection).toContain(leaveApplication);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decisions = { id: 123 };
        spyOn(decisionsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decisions }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(decisionsService.update).toHaveBeenCalledWith(decisions);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decisions = new Decisions();
        spyOn(decisionsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decisions }));
        saveSubject.complete();

        // THEN
        expect(decisionsService.create).toHaveBeenCalledWith(decisions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decisions = { id: 123 };
        spyOn(decisionsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decisions });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(decisionsService.update).toHaveBeenCalledWith(decisions);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommentById', () => {
        it('Should return tracked Comment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLeaveApplicationById', () => {
        it('Should return tracked LeaveApplication primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeaveApplicationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
