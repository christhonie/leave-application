package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An entitlement represents an instance when leave days are assigned to zero, one or more Staff members.\nEntitlements are typically assigned at the start of an entitlement period, such as the first day of the month.\nAlso see {@link EntitlementValue} for how the days are assigned to the {@link Staff} members.
 */
@Entity
@Table(name = "leave_entitlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaveEntitlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The date the entitlement is applied to the Staff member.
     */
    @NotNull
    @Column(name = "entitlement_date", nullable = false)
    private LocalDate entitlementDate;

    /**
     * @deprecated Use the EntitlementValue
     */
    @NotNull
    @Column(name = "days", precision = 21, scale = 2, nullable = false)
    private BigDecimal days;

    /**
     * The leave type this entitlement applies to.
     */
    @ManyToOne(optional = false)
    @NotNull
    private LeaveType leaveType;

    /**
     * The staff member the leave is assigned to.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "teams" }, allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveEntitlement id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getEntitlementDate() {
        return this.entitlementDate;
    }

    public LeaveEntitlement entitlementDate(LocalDate entitlementDate) {
        this.entitlementDate = entitlementDate;
        return this;
    }

    public void setEntitlementDate(LocalDate entitlementDate) {
        this.entitlementDate = entitlementDate;
    }

    public BigDecimal getDays() {
        return this.days;
    }

    public LeaveEntitlement days(BigDecimal days) {
        this.days = days;
        return this;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public LeaveEntitlement leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public LeaveEntitlement staff(Staff staff) {
        this.setStaff(staff);
        return this;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveEntitlement)) {
            return false;
        }
        return id != null && id.equals(((LeaveEntitlement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveEntitlement{" +
            "id=" + getId() +
            ", entitlementDate='" + getEntitlementDate() + "'" +
            ", days=" + getDays() +
            "}";
    }
}
