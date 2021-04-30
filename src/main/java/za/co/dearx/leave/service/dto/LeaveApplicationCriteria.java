package za.co.dearx.leave.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.LeaveApplication} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.LeaveApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leave-applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveApplicationCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private ZonedDateTimeFilter appliedDate;

    private ZonedDateTimeFilter updateDate;

    private BigDecimalFilter days;

    private BooleanFilter deleted;

    private LongFilter leaveTypeId;

    private LongFilter leaveStatusId;

    private LongFilter staffId;

    public LeaveApplicationCriteria() {}

    public LeaveApplicationCriteria(LeaveApplicationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.appliedDate = other.appliedDate == null ? null : other.appliedDate.copy();
        this.updateDate = other.updateDate == null ? null : other.updateDate.copy();
        this.days = other.days == null ? null : other.days.copy();
        this.deleted = other.deleted == null ? null : other.deleted.copy();
        this.leaveTypeId = other.leaveTypeId == null ? null : other.leaveTypeId.copy();
        this.leaveStatusId = other.leaveStatusId == null ? null : other.leaveStatusId.copy();
        this.staffId = other.staffId == null ? null : other.staffId.copy();
    }

    @Override
    public LeaveApplicationCriteria copy() {
        return new LeaveApplicationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTimeFilter getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(ZonedDateTimeFilter appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ZonedDateTimeFilter getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTimeFilter updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimalFilter getDays() {
        return days;
    }

    public void setDays(BigDecimalFilter days) {
        this.days = days;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public LongFilter getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(LongFilter leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public LongFilter getLeaveStatusId() {
        return leaveStatusId;
    }

    public void setLeaveStatusId(LongFilter leaveStatusId) {
        this.leaveStatusId = leaveStatusId;
    }

    public LongFilter getStaffId() {
        return staffId;
    }

    public void setStaffId(LongFilter staffId) {
        this.staffId = staffId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeaveApplicationCriteria that = (LeaveApplicationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(appliedDate, that.appliedDate) &&
            Objects.equals(updateDate, that.updateDate) &&
            Objects.equals(days, that.days) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(leaveTypeId, that.leaveTypeId) &&
            Objects.equals(leaveStatusId, that.leaveStatusId) &&
            Objects.equals(staffId, that.staffId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, appliedDate, updateDate, days, deleted, leaveTypeId, leaveStatusId, staffId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveApplicationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (appliedDate != null ? "appliedDate=" + appliedDate + ", " : "") +
                (updateDate != null ? "updateDate=" + updateDate + ", " : "") +
                (days != null ? "days=" + days + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
                (leaveTypeId != null ? "leaveTypeId=" + leaveTypeId + ", " : "") +
                (leaveStatusId != null ? "leaveStatusId=" + leaveStatusId + ", " : "") +
                (staffId != null ? "staffId=" + staffId + ", " : "") +
            "}";
    }
}
