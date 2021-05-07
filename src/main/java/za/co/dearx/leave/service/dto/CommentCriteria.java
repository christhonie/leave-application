package za.co.dearx.leave.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.Comment} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comment;

    private LongFilter leaveApplicationId;

    private LongFilter userId;

    public CommentCriteria() {}

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.leaveApplicationId = other.leaveApplicationId == null ? null : other.leaveApplicationId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(LongFilter leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(leaveApplicationId, that.leaveApplicationId) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, leaveApplicationId, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (leaveApplicationId != null ? "leaveApplicationId=" + leaveApplicationId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
