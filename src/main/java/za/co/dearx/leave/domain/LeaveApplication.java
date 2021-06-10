package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An application for leave by a {@link Staff} member.
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

    /**
     * The leave type of this application. This field cannot be changed once the record is created.
     */
    @ManyToOne(optional = false)
    @NotNull
    private LeaveType leaveType;

    @NotNull
    @ManyToOne(optional = false)
    @JsonIgnoreProperties(value = "leaveApplications", allowSetters = true)
    private LeaveStatus leaveStatus;

    /**
     * The Staff member the leave is assigned to. This field cannot be changed once the record is created.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "teams" }, allowSetters = true)
    private Staff staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveApplication id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LeaveApplication startDate(LocalDate startDate) {
        setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        calculateDays();
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public LeaveApplication endDate(LocalDate endDate) {
        setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        calculateDays();
    }

    public ZonedDateTime getAppliedDate() {
        return this.appliedDate;
    }

    public LeaveApplication appliedDate(ZonedDateTime appliedDate) {
        this.appliedDate = appliedDate;
        return this;
    }

    public void setAppliedDate(ZonedDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ZonedDateTime getUpdateDate() {
        return this.updateDate;
    }

    public LeaveApplication updateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getDays() {
        return this.days;
    }

    public LeaveApplication days(BigDecimal days) {
        setDays(days);
        return this;
    }

    /**
     * Calling this method has no effect.
     * This value is set using {@link #setStartDate(LocalDate)} and/or {@link #setEndDate(LocalDate)}.
     *
     * @param days
     */
    public void setDays(BigDecimal days) {
        //Do not allow direct setting.
    }

    private void calculateDays() {
        if (startDate != null && endDate != null) {
            Period difference = Period.between(startDate, endDate);
            this.days = BigDecimal.valueOf(difference.getDays());
        } else {
            days = null;
        }
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public LeaveApplication deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public LeaveApplication leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveStatus getLeaveStatus() {
        return this.leaveStatus;
    }

    public LeaveApplication leaveStatus(LeaveStatus leaveStatus) {
        this.setLeaveStatus(leaveStatus);
        return this;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Staff getStaff() {
        return this.staff;
    }

    public LeaveApplication staff(Staff staff) {
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
        if (!(o instanceof LeaveApplication)) {
            return false;
        }
        return id != null && id.equals(((LeaveApplication) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
