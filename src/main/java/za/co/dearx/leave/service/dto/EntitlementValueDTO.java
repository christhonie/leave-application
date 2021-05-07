package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.EntitlementValue} entity.
 */
public class EntitlementValueDTO implements Serializable {
    private Long id;

    @NotNull
    private BigDecimal entitlementValue;

    private Long entitlementId;

    private Long staffId;

    private String staffName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEntitlementValue() {
        return entitlementValue;
    }

    public void setEntitlementValue(BigDecimal entitlementValue) {
        this.entitlementValue = entitlementValue;
    }

    public Long getEntitlementId() {
        return entitlementId;
    }

    public void setEntitlementId(Long leaveEntitlementId) {
        this.entitlementId = leaveEntitlementId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntitlementValueDTO)) {
            return false;
        }

        return id != null && id.equals(((EntitlementValueDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntitlementValueDTO{" +
            "id=" + getId() +
            ", entitlementValue=" + getEntitlementValue() +
            ", entitlementId=" + getEntitlementId() +
            ", staffId=" + getStaffId() +
            ", staffName='" + getStaffName() + "'" +
            "}";
    }
}
