package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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

    private Long leaveTypeId;

    private String leaveTypeName;

    private Long leaveStatusId;

    private String leaveStatusName;

    private Long staffId;

    private String staffName;

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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public Long getLeaveStatusId() {
        return leaveStatusId;
    }

    public void setLeaveStatusId(Long leaveStatusId) {
        this.leaveStatusId = leaveStatusId;
    }

    public String getLeaveStatusName() {
        return leaveStatusName;
    }

    public void setLeaveStatusName(String leaveStatusName) {
        this.leaveStatusName = leaveStatusName;
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
        if (!(o instanceof LeaveApplicationDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveApplicationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
            ", deleted='" + isDeleted() + "'" +
            ", leaveTypeId=" + getLeaveTypeId() +
            ", leaveTypeName='" + getLeaveTypeName() + "'" +
            ", leaveStatusId=" + getLeaveStatusId() +
            ", leaveStatusName='" + getLeaveStatusName() + "'" +
            ", staffId=" + getStaffId() +
            ", staffName='" + getStaffName() + "'" +
            "}";
    }
}
