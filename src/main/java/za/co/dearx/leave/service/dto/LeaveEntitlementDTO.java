package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
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
            ", days=" + getDays() +
            ", leaveType=" + getLeaveType() +
            ", staff=" + getStaff() +
            "}";
    }
}
