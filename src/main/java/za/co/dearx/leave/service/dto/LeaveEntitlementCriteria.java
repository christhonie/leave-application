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
import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.LeaveEntitlement} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.LeaveEntitlementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leave-entitlements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveEntitlementCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter entitlementDate;

    private BigDecimalFilter days;

    private LongFilter leaveTypeId;

    private LongFilter staffId;

    public LeaveEntitlementCriteria() {}

    public LeaveEntitlementCriteria(LeaveEntitlementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entitlementDate = other.entitlementDate == null ? null : other.entitlementDate.copy();
        this.days = other.days == null ? null : other.days.copy();
        this.leaveTypeId = other.leaveTypeId == null ? null : other.leaveTypeId.copy();
        this.staffId = other.staffId == null ? null : other.staffId.copy();
    }

    @Override
    public LeaveEntitlementCriteria copy() {
        return new LeaveEntitlementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getEntitlementDate() {
        return entitlementDate;
    }

    public void setEntitlementDate(LocalDateFilter entitlementDate) {
        this.entitlementDate = entitlementDate;
    }

    public BigDecimalFilter getDays() {
        return days;
    }

    public void setDays(BigDecimalFilter days) {
        this.days = days;
    }

    public LongFilter getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(LongFilter leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
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
        final LeaveEntitlementCriteria that = (LeaveEntitlementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entitlementDate, that.entitlementDate) &&
            Objects.equals(days, that.days) &&
            Objects.equals(leaveTypeId, that.leaveTypeId) &&
            Objects.equals(staffId, that.staffId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entitlementDate, days, leaveTypeId, staffId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveEntitlementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (entitlementDate != null ? "entitlementDate=" + entitlementDate + ", " : "") +
                (days != null ? "days=" + days + ", " : "") +
                (leaveTypeId != null ? "leaveTypeId=" + leaveTypeId + ", " : "") +
                (staffId != null ? "staffId=" + staffId + ", " : "") +
            "}";
    }
}
