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
 * A LeaveEntitlement.
 */
@Entity
@Table(name = "leave_entitlement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaveEntitlement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entitlement_date", nullable = false)
    private LocalDate entitlementDate;

    @NotNull
    @Column(name = "days", precision = 21, scale = 2, nullable = false)
    private BigDecimal days;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "leaveEntitlements", allowSetters = true)
    private LeaveType leaveType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "leaveEntitlements", allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntitlementDate() {
        return entitlementDate;
    }

    public LeaveEntitlement entitlementDate(LocalDate entitlementDate) {
        this.entitlementDate = entitlementDate;
        return this;
    }

    public void setEntitlementDate(LocalDate entitlementDate) {
        this.entitlementDate = entitlementDate;
    }

    public BigDecimal getDays() {
        return days;
    }

    public LeaveEntitlement days(BigDecimal days) {
        this.days = days;
        return this;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public LeaveEntitlement leaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
        return this;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Staff getStaff() {
        return staff;
    }

    public LeaveEntitlement staff(Staff staff) {
        this.staff = staff;
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
        return 31;
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
