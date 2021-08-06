package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A mechanism to track leave deducted from one or more LeaveEntitlements for each LeaveApplication, which helps track how much, if any, leave is still remaining.
 */
@Entity
@Table(name = "leave_deduction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaveDeduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "days", precision = 21, scale = 2, nullable = false)
    private BigDecimal days;

    /**
     * The LeaveApplication for which this deduction applies.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaveType", "leaveStatus", "staff", "deductions" }, allowSetters = true)
    private LeaveApplication application;

    /**
     * The LeaveEntitlement to reduce.
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "leaveType", "staff", "deductions" }, allowSetters = true)
    private LeaveEntitlement entitlement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveDeduction id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getDays() {
        return this.days;
    }

    public LeaveDeduction days(BigDecimal days) {
        this.days = days;
        return this;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public LeaveApplication getApplication() {
        return this.application;
    }

    public LeaveDeduction application(LeaveApplication leaveApplication) {
        this.setApplication(leaveApplication);
        return this;
    }

    public void setApplication(LeaveApplication leaveApplication) {
        this.application = leaveApplication;
    }

    public LeaveEntitlement getEntitlement() {
        return this.entitlement;
    }

    public LeaveDeduction entitlement(LeaveEntitlement leaveEntitlement) {
        this.setEntitlement(leaveEntitlement);
        return this;
    }

    public void setEntitlement(LeaveEntitlement leaveEntitlement) {
        this.entitlement = leaveEntitlement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveDeduction)) {
            return false;
        }
        return id != null && id.equals(((LeaveDeduction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDeduction{" +
            "id=" + getId() +
            ", days=" + getDays() +
            "}";
    }
}
