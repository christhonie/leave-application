jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommentService } from '../service/comment.service';
import { IComment, Comment } from '../comment.model';
import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CommentUpdateComponent } from './comment-update.component';

describe('Component Tests', () => {
  describe('Comment Management Update Component', () => {
    let comp: CommentUpdateComponent;
    let fixture: ComponentFixture<CommentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commentService: CommentService;
    let leaveApplicationService: LeaveApplicationService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commentService = TestBed.inject(CommentService);
      leaveApplicationService = TestBed.inject(LeaveApplicationService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LeaveApplication query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const leaveApplication: ILeaveApplication = { id: 66828 };
        comment.leaveApplication = leaveApplication;

        const leaveApplicationCollection: ILeaveApplication[] = [{ id: 66137 }];
        spyOn(leaveApplicationService, 'query').and.returnValue(of(new HttpResponse({ body: leaveApplicationCollection })));
        const additionalLeaveApplications = [leaveApplication];
        const expectedCollection: ILeaveApplication[] = [...additionalLeaveApplications, ...leaveApplicationCollection];
        spyOn(leaveApplicationService, 'addLeaveApplicationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(leaveApplicationService.query).toHaveBeenCalled();
        expect(leaveApplicationService.addLeaveApplicationToCollectionIfMissing).toHaveBeenCalledWith(
          leaveApplicationCollection,
          ...additionalLeaveApplications
        );
        expect(comp.leaveApplicationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const user: IUser = { id: 38932 };
        comment.user = user;

        const userCollection: IUser[] = [{ id: 48070 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const comment: IComment = { id: 456 };
        const leaveApplication: ILeaveApplication = { id: 48170 };
        comment.leaveApplication = leaveApplication;
        const user: IUser = { id: 42031 };
        comment.user = user;

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comment));
        expect(comp.leaveApplicationsSharedCollection).toContain(leaveApplication);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commentService.update).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = new Comment();
        spyOn(commentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(commentService.create).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commentService.update).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLeaveApplicationById', () => {
        it('Should return tracked LeaveApplication primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeaveApplicationById(0, entity);
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
    });
  });
});
