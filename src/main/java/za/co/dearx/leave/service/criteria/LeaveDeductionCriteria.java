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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.LeaveDeduction} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.LeaveDeductionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leave-deductions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveDeductionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter days;

    private LongFilter applicationId;

    private LongFilter entitlementId;

    public LeaveDeductionCriteria() {}

    public LeaveDeductionCriteria(LeaveDeductionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.days = other.days == null ? null : other.days.copy();
        this.applicationId = other.applicationId == null ? null : other.applicationId.copy();
        this.entitlementId = other.entitlementId == null ? null : other.entitlementId.copy();
    }

    @Override
    public LeaveDeductionCriteria copy() {
        return new LeaveDeductionCriteria(this);
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

    public LongFilter getApplicationId() {
        return applicationId;
    }

    public LongFilter applicationId() {
        if (applicationId == null) {
            applicationId = new LongFilter();
        }
        return applicationId;
    }

    public void setApplicationId(LongFilter applicationId) {
        this.applicationId = applicationId;
    }

    public LongFilter getEntitlementId() {
        return entitlementId;
    }

    public LongFilter entitlementId() {
        if (entitlementId == null) {
            entitlementId = new LongFilter();
        }
        return entitlementId;
    }

    public void setEntitlementId(LongFilter entitlementId) {
        this.entitlementId = entitlementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeaveDeductionCriteria that = (LeaveDeductionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(days, that.days) &&
            Objects.equals(applicationId, that.applicationId) &&
            Objects.equals(entitlementId, that.entitlementId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, days, applicationId, entitlementId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDeductionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (days != null ? "days=" + days + ", " : "") +
            (applicationId != null ? "applicationId=" + applicationId + ", " : "") +
            (entitlementId != null ? "entitlementId=" + entitlementId + ", " : "") +
            "}";
    }
}
