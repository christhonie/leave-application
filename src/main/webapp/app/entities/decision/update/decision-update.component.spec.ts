jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DecisionService } from '../service/decision.service';
import { IDecision, Decision } from '../decision.model';
import { IComment } from 'app/entities/comment/comment.model';
import { CommentService } from 'app/entities/comment/service/comment.service';

import { ILeaveApplication } from 'app/entities/leave-application/leave-application.model';
import { LeaveApplicationService } from 'app/entities/leave-application/service/leave-application.service';

import { DecisionUpdateComponent } from './decision-update.component';

describe('Component Tests', () => {
  describe('Decision Management Update Component', () => {
    let comp: DecisionUpdateComponent;
    let fixture: ComponentFixture<DecisionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let decisionService: DecisionService;
    let commentService: CommentService;

    let leaveApplicationService: LeaveApplicationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DecisionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DecisionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DecisionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      decisionService = TestBed.inject(DecisionService);
      commentService = TestBed.inject(CommentService);
      leaveApplicationService = TestBed.inject(LeaveApplicationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call comment query and add missing value', () => {
        const decision: IDecision = { id: 456 };
        const comment: IComment = { id: 97824 };
        decision.comment = comment;

        const commentCollection: IComment[] = [{ id: 79563 }];
        spyOn(commentService, 'query').and.returnValue(of(new HttpResponse({ body: commentCollection })));
        const expectedCollection: IComment[] = [comment, ...commentCollection];
        spyOn(commentService, 'addCommentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        expect(commentService.query).toHaveBeenCalled();
        expect(commentService.addCommentToCollectionIfMissing).toHaveBeenCalledWith(commentCollection, comment);
        expect(comp.commentsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const decision: IDecision = { id: 456 };
        activatedRoute.data = of({ decision });
        comp.ngOnInit();
      });

      it('Should call LeaveApplication query and add missing value', () => {
        const decision: IDecision = { id: 456 };
        const leaveApplication: ILeaveApplication = { id: 98150 };
        decision.leaveApplication = leaveApplication;

        const leaveApplicationCollection: ILeaveApplication[] = [{ id: 58285 }];
        spyOn(leaveApplicationService, 'query').and.returnValue(of(new HttpResponse({ body: leaveApplicationCollection })));
        const additionalLeaveApplications = [leaveApplication];
        const expectedCollection: ILeaveApplication[] = [...additionalLeaveApplications, ...leaveApplicationCollection];
        spyOn(leaveApplicationService, 'addLeaveApplicationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        expect(leaveApplicationService.query).toHaveBeenCalled();
        expect(leaveApplicationService.addLeaveApplicationToCollectionIfMissing).toHaveBeenCalledWith(
          leaveApplicationCollection,
          ...additionalLeaveApplications
        );
        expect(comp.leaveApplicationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const decision: IDecision = { id: 456 };
        const comment: IComment = { id: 94002 };
        decision.comment = comment;
        const leaveApplication: ILeaveApplication = { id: 19592 };
        decision.leaveApplication = leaveApplication;

        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(decision));
        expect(comp.commentsCollection).toContain(comment);
        expect(comp.leaveApplicationsSharedCollection).toContain(leaveApplication);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decision = { id: 123 };
        spyOn(decisionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decision }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(decisionService.update).toHaveBeenCalledWith(decision);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decision = new Decision();
        spyOn(decisionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: decision }));
        saveSubject.complete();

        // THEN
        expect(decisionService.create).toHaveBeenCalledWith(decision);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const decision = { id: 123 };
        spyOn(decisionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ decision });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(decisionService.update).toHaveBeenCalledWith(decision);
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
