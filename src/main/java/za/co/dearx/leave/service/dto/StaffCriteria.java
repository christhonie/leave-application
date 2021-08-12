package za.co.dearx.leave.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;

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

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter contractNumber;

    private StringFilter gender;

    private LongFilter userId;

    private LongFilter teamId;

    public StaffCriteria() {}

    public StaffCriteria(StaffCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.employeeID = other.employeeID == null ? null : other.employeeID.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.contractNumber = other.contractNumber == null ? null : other.contractNumber.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
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

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(StringFilter employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public StringFilter getGender() {
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTeamId() {
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
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(teamId, that.teamId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, employeeID, startDate, firstName, lastName, email, contractNumber, gender, userId, teamId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (employeeID != null ? "employeeID=" + employeeID + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (contractNumber != null ? "contractNumber=" + contractNumber + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (teamId != null ? "teamId=" + teamId + ", " : "") +
            "}";
    }
}
