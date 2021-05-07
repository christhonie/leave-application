package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaveApplication.
 */
@Entity
@Table(name = "leave_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaveApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "applied_date", nullable = false)
    private ZonedDateTime appliedDate;

    @Column(name = "update_date")
    private ZonedDateTime updateDate;

    @NotNull
    @Column(name = "days", precision = 21, scale = 2, nullable = false)
    private BigDecimal days;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "leaveApplications", allowSetters = true)
    private LeaveType leaveType;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = "leaveApplications", allowSetters = true)
    private LeaveStatus leaveStatus;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "leaveApplications", allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LeaveApplication startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LeaveApplication endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getAppliedDate() {
        return appliedDate;
    }

    public LeaveApplication appliedDate(ZonedDateTime appliedDate) {
        this.appliedDate = appliedDate;
        return this;
    }

    public void setAppliedDate(ZonedDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public LeaveApplication updateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getDays() {
        return days;
    }

    public LeaveApplication days(BigDecimal days) {
        this.days = days;
        return this;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public LeaveApplication deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public LeaveApplication leaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
        return this;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public LeaveApplication leaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
        return this;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Staff getStaff() {
        return staff;
    }

    public LeaveApplication staff(Staff staff) {
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
        if (!(o instanceof LeaveApplication)) {
            return false;
        }
        return id != null && id.equals(((LeaveApplication) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveApplication{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", appliedDate='" + getAppliedDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", days=" + getDays() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
