package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;
import za.co.dearx.leave.domain.Staff;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.EntitlementValue} entity.
 */
@ApiModel(description = "The allocation of leave days for a given {@link LeaveEntitlement} and {@link Staff} member.")
public class EntitlementValueDTO implements Serializable {

    private Long id;

    /**
     * The value, in days, assigned to a {@link Staff} member.
     */
    @NotNull
    @ApiModelProperty(value = "The value, in days, assigned to a {@link Staff} member.", required = true)
    private BigDecimal entitlementValue;

    private LeaveEntitlementDTO entitlement;

    private StaffDTO staff;

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

    public LeaveEntitlementDTO getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(LeaveEntitlementDTO entitlement) {
        this.entitlement = entitlement;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntitlementValueDTO)) {
            return false;
        }

        EntitlementValueDTO entitlementValueDTO = (EntitlementValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entitlementValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntitlementValueDTO{" +
            "id=" + getId() +
            ", entitlementValue=" + getEntitlementValue() +
            ", entitlement=" + getEntitlement() +
            ", staff=" + getStaff() +
            "}";
    }
}
