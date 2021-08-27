package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveEntitlement} entity.
 */
@ApiModel(
    description = "An entitlement represents an instance when leave days are assigned to zero, one or more Staff members. " +
    "Entitlements are typically assigned at the start of an entitlement period, such as the first day of the month. " +
    "Also see EntitlementValue for how the days are assigned to the Staff members."
)
public class LeaveEntitlementDTO implements Serializable {

    private Long id;

    /**
     * The date the entitlement is applied to the Staff member.
     */
    @NotNull
    @ApiModelProperty(value = "The date the entitlement is applied to the Staff member.", required = true)
    private LocalDate entitlementDate;

    /**
     * The date when this entitlement should expire.
     */
    @NotNull
    @ApiModelProperty(value = "The date when this entitlement should expire.", required = true)
    private LocalDate expiryDate;

    /**
     * Use the EntitlementValue
     */
    @NotNull
    @ApiModelProperty(value = "Use the EntitlementValue", required = true)
    private BigDecimal days;

    private LeaveTypeDTO leaveType;

    private StaffDTO staff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntitlementDate() {
        return entitlementDate;
    }

    public void setEntitlementDate(LocalDate entitlementDate) {
        this.entitlementDate = entitlementDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
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
        if (!(o instanceof LeaveEntitlementDTO)) {
            return false;
        }

        LeaveEntitlementDTO leaveEntitlementDTO = (LeaveEntitlementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveEntitlementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveEntitlementDTO{" +
            "id=" + getId() +
            ", entitlementDate='" + getEntitlementDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", days=" + getDays() +
            ", leaveType=" + getLeaveType() +
            ", staff=" + getStaff() +
            "}";
    }
}
