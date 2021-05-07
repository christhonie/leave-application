package za.co.dearx.leave.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.Decisions} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.DecisionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DecisionsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DecisionChoice
     */
    public static class DecisionChoiceFilter extends Filter<DecisionChoice> {

        public DecisionChoiceFilter() {}

        public DecisionChoiceFilter(DecisionChoiceFilter filter) {
            super(filter);
        }

        @Override
        public DecisionChoiceFilter copy() {
            return new DecisionChoiceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DecisionChoiceFilter choice;

    private InstantFilter decidedOn;

    private LongFilter commentId;

    private LongFilter userId;

    private LongFilter leaveApplicationId;

    public DecisionsCriteria() {}

    public DecisionsCriteria(DecisionsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.choice = other.choice == null ? null : other.choice.copy();
        this.decidedOn = other.decidedOn == null ? null : other.decidedOn.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.leaveApplicationId = other.leaveApplicationId == null ? null : other.leaveApplicationId.copy();
    }

    @Override
    public DecisionsCriteria copy() {
        return new DecisionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DecisionChoiceFilter getChoice() {
        return choice;
    }

    public DecisionChoiceFilter choice() {
        if (choice == null) {
            choice = new DecisionChoiceFilter();
        }
        return choice;
    }

    public void setChoice(DecisionChoiceFilter choice) {
        this.choice = choice;
    }

    public InstantFilter getDecidedOn() {
        return decidedOn;
    }

    public InstantFilter decidedOn() {
        if (decidedOn == null) {
            decidedOn = new InstantFilter();
        }
        return decidedOn;
    }

    public void setDecidedOn(InstantFilter decidedOn) {
        this.decidedOn = decidedOn;
    }

    public LongFilter getCommentId() {
        return commentId;
    }

    public LongFilter commentId() {
        if (commentId == null) {
            commentId = new LongFilter();
        }
        return commentId;
    }

    public void setCommentId(LongFilter commentId) {
        this.commentId = commentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public LongFilter leaveApplicationId() {
        if (leaveApplicationId == null) {
            leaveApplicationId = new LongFilter();
        }
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(LongFilter leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DecisionsCriteria that = (DecisionsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(choice, that.choice) &&
            Objects.equals(decidedOn, that.decidedOn) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(leaveApplicationId, that.leaveApplicationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, choice, decidedOn, commentId, userId, leaveApplicationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (choice != null ? "choice=" + choice + ", " : "") +
            (decidedOn != null ? "decidedOn=" + decidedOn + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (leaveApplicationId != null ? "leaveApplicationId=" + leaveApplicationId + ", " : "") +
            "}";
    }
}
