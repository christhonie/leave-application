package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveApplication} entity.
 */
public class LeaveApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private ZonedDateTime appliedDate;

    private ZonedDateTime updateDate;

    @NotNull
    private BigDecimal days;

    @NotNull
    private Boolean deleted;

    private LeaveTypeDTO leaveType;

    private LeaveStatusDTO leaveStatus;

    private StaffDTO staff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(ZonedDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveStatusDTO getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatusDTO leaveStatus) {
        this.leaveStatus = leaveStatus;
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
        if (!(o instanceof LeaveApplicationDTO)) {
            return false;
        }

        LeaveApplicationDTO leaveApplicationDTO = (LeaveApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveApplicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveApplicationDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", appliedDate='" + getAppliedDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", days=" + getDays() +
            ", deleted='" + getDeleted() + "'" +
            ", leaveType=" + getLeaveType() +
            ", leaveStatus=" + getLeaveStatus() +
            ", staff=" + getStaff() +
            "}";
    }
}
