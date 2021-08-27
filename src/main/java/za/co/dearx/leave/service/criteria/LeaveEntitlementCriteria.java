package za.co.dearx.leave.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

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

    private LocalDateFilter expiryDate;

    private BigDecimalFilter days;

    private LongFilter leaveTypeId;

    private LongFilter staffId;

    private LongFilter deductionId;

    public LeaveEntitlementCriteria() {}

    public LeaveEntitlementCriteria(LeaveEntitlementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entitlementDate = other.entitlementDate == null ? null : other.entitlementDate.copy();
        this.expiryDate = other.expiryDate == null ? null : other.expiryDate.copy();
        this.days = other.days == null ? null : other.days.copy();
        this.leaveTypeId = other.leaveTypeId == null ? null : other.leaveTypeId.copy();
        this.staffId = other.staffId == null ? null : other.staffId.copy();
        this.deductionId = other.deductionId == null ? null : other.deductionId.copy();
    }

    @Override
    public LeaveEntitlementCriteria copy() {
        return new LeaveEntitlementCriteria(this);
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

    public LocalDateFilter getEntitlementDate() {
        return entitlementDate;
    }

    public LocalDateFilter entitlementDate() {
        if (entitlementDate == null) {
            entitlementDate = new LocalDateFilter();
        }
        return entitlementDate;
    }

    public void setEntitlementDate(LocalDateFilter entitlementDate) {
        this.entitlementDate = entitlementDate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public LocalDateFilter expiryDate() {
        if (expiryDate == null) {
            expiryDate = new LocalDateFilter();
        }
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimalFilter getDays() {
        return days;
    }

    public BigDecimalFilter days() {
        if (days == null) {
            days = new BigDecimalFilter();
        }
        return days;
    }

    public void setDays(BigDecimalFilter days) {
        this.days = days;
    }

    public LongFilter getLeaveTypeId() {
        return leaveTypeId;
    }

    public LongFilter leaveTypeId() {
        if (leaveTypeId == null) {
            leaveTypeId = new LongFilter();
        }
        return leaveTypeId;
    }

    public void setLeaveTypeId(LongFilter leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public LongFilter getStaffId() {
        return staffId;
    }

    public LongFilter staffId() {
        if (staffId == null) {
            staffId = new LongFilter();
        }
        return staffId;
    }

    public void setStaffId(LongFilter staffId) {
        this.staffId = staffId;
    }

    public LongFilter getDeductionId() {
        return deductionId;
    }

    public LongFilter deductionId() {
        if (deductionId == null) {
            deductionId = new LongFilter();
        }
        return deductionId;
    }

    public void setDeductionId(LongFilter deductionId) {
        this.deductionId = deductionId;
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
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(days, that.days) &&
            Objects.equals(leaveTypeId, that.leaveTypeId) &&
            Objects.equals(staffId, that.staffId) &&
            Objects.equals(deductionId, that.deductionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entitlementDate, expiryDate, days, leaveTypeId, staffId, deductionId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveEntitlementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entitlementDate != null ? "entitlementDate=" + entitlementDate + ", " : "") +
            (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
            (days != null ? "days=" + days + ", " : "") +
            (leaveTypeId != null ? "leaveTypeId=" + leaveTypeId + ", " : "") +
            (staffId != null ? "staffId=" + staffId + ", " : "") +
            (deductionId != null ? "deductionId=" + deductionId + ", " : "") +
            "}";
    }
}
