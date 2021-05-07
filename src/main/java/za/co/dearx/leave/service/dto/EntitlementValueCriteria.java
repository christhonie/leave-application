package za.co.dearx.leave.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BigDecimalFilter;
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
 * Criteria class for the {@link za.co.dearx.leave.domain.EntitlementValue} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.EntitlementValueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /entitlement-values?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EntitlementValueCriteria implements Serializable, Criteria {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter entitlementValue;

    private LongFilter entitlementId;

    private LongFilter staffId;

    public EntitlementValueCriteria() {}

    public EntitlementValueCriteria(EntitlementValueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entitlementValue = other.entitlementValue == null ? null : other.entitlementValue.copy();
        this.entitlementId = other.entitlementId == null ? null : other.entitlementId.copy();
        this.staffId = other.staffId == null ? null : other.staffId.copy();
    }

    @Override
    public EntitlementValueCriteria copy() {
        return new EntitlementValueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getEntitlementValue() {
        return entitlementValue;
    }

    public void setEntitlementValue(BigDecimalFilter entitlementValue) {
        this.entitlementValue = entitlementValue;
    }

    public LongFilter getEntitlementId() {
        return entitlementId;
    }

    public void setEntitlementId(LongFilter entitlementId) {
        this.entitlementId = entitlementId;
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
        final EntitlementValueCriteria that = (EntitlementValueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entitlementValue, that.entitlementValue) &&
            Objects.equals(entitlementId, that.entitlementId) &&
            Objects.equals(staffId, that.staffId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entitlementValue, entitlementId, staffId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntitlementValueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (entitlementValue != null ? "entitlementValue=" + entitlementValue + ", " : "") +
                (entitlementId != null ? "entitlementId=" + entitlementId + ", " : "") +
                (staffId != null ? "staffId=" + staffId + ", " : "") +
            "}";
    }
}
