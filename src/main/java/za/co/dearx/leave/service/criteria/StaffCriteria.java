package za.co.dearx.leave.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link za.co.dearx.leave.domain.Staff} entity. This class is used
 * in {@link za.co.dearx.leave.web.rest.StaffResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /staff?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StaffCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter position;

    private StringFilter employeeID;

    private LocalDateFilter startDate;

    private StringFilter name;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter contractNumber;

    private StringFilter gender;

    private LongFilter annualLeaveEntitlement;

    private LongFilter userId;

    private LongFilter teamId;

    public StaffCriteria() {}

    public StaffCriteria(StaffCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.employeeID = other.employeeID == null ? null : other.employeeID.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.contractNumber = other.contractNumber == null ? null : other.contractNumber.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.annualLeaveEntitlement = other.annualLeaveEntitlement == null ? null : other.annualLeaveEntitlement.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
    }

    @Override
    public StaffCriteria copy() {
        return new StaffCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPosition() {
        return position;
    }

    public StringFilter position() {
        if (position == null) {
            position = new StringFilter();
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getEmployeeID() {
        return employeeID;
    }

    public StringFilter employeeID() {
        if (employeeID == null) {
            employeeID = new StringFilter();
        }
        return employeeID;
    }

    public void setEmployeeID(StringFilter employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public StringFilter contractNumber() {
        if (contractNumber == null) {
            contractNumber = new StringFilter();
        }
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public StringFilter getGender() {
        return gender;
    }

    public StringFilter gender() {
        if (gender == null) {
            gender = new StringFilter();
        }
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public LongFilter getAnnualLeaveEntitlement() {
        return annualLeaveEntitlement;
    }

    public LongFilter annualLeaveEntitlement() {
        if (annualLeaveEntitlement == null) {
            annualLeaveEntitlement = new LongFilter();
        }
        return annualLeaveEntitlement;
    }

    public void setAnnualLeaveEntitlement(LongFilter annualLeaveEntitlement) {
        this.annualLeaveEntitlement = annualLeaveEntitlement;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public LongFilter teamId() {
        if (teamId == null) {
            teamId = new LongFilter();
        }
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StaffCriteria that = (StaffCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(position, that.position) &&
            Objects.equals(employeeID, that.employeeID) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(annualLeaveEntitlement, that.annualLeaveEntitlement) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(teamId, that.teamId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            position,
            employeeID,
            startDate,
            name,
            firstName,
            lastName,
            email,
            contractNumber,
            gender,
            annualLeaveEntitlement,
            userId,
            teamId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (employeeID != null ? "employeeID=" + employeeID + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (contractNumber != null ? "contractNumber=" + contractNumber + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (annualLeaveEntitlement != null ? "annualLeaveEntitlement=" + annualLeaveEntitlement + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (teamId != null ? "teamId=" + teamId + ", " : "") +
            "}";
    }
}
