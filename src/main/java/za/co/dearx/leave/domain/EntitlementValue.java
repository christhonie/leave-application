package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EntitlementValue.
 */
@Entity
@Table(name = "entitlement_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EntitlementValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entitlement_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal entitlementValue;

    @JsonIgnoreProperties(value = { "leaveType", "staff" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private LeaveEntitlement entitlement;

    @JsonIgnoreProperties(value = { "user", "teams" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntitlementValue id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getEntitlementValue() {
        return this.entitlementValue;
    }

    public EntitlementValue entitlementValue(BigDecimal entitlementValue) {
        this.entitlementValue = entitlementValue;
        return this;
    }

    public void setEntitlementValue(BigDecimal entitlementValue) {
        this.entitlementValue = entitlementValue;
    }

    public LeaveEntitlement getEntitlement() {
        return this.entitlement;
    }

    public EntitlementValue entitlement(LeaveEntitlement leaveEntitlement) {
        this.setEntitlement(leaveEntitlement);
        return this;
    }

    public void setEntitlement(LeaveEntitlement leaveEntitlement) {
        this.entitlement = leaveEntitlement;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public EntitlementValue staff(Staff staff) {
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
        if (!(o instanceof EntitlementValue)) {
            return false;
        }
        return id != null && id.equals(((EntitlementValue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntitlementValue{" +
            "id=" + getId() +
            ", entitlementValue=" + getEntitlementValue() +
            "}";
    }
}
