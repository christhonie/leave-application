package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveEntitlement} entity.
 */
public class LeaveEntitlementDTO implements Serializable {
    private Long id;

    @NotNull
    private LocalDate entitlementDate;

    @NotNull
    private BigDecimal days;

    private Long leaveTypeId;

    private String leaveTypeName;

    private Long staffId;

    private String staffName;

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

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public Long getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Long leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
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
        if (!(o instanceof LeaveEntitlementDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveEntitlementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveEntitlementDTO{" +
            "id=" + getId() +
            ", entitlementDate='" + getEntitlementDate() + "'" +
            ", days=" + getDays() +
            ", leaveTypeId=" + getLeaveTypeId() +
            ", leaveTypeName='" + getLeaveTypeName() + "'" +
            ", staffId=" + getStaffId() +
            ", staffName='" + getStaffName() + "'" +
            "}";
    }
}
