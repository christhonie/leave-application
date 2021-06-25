package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.Staff} entity.
 */
@ApiModel(
    description = "An employee of the company.\nThe person can be linked to zero or more teams and may be linked to a User of the system."
)
public class StaffDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String position;

    /**
     * The unique employee number assigned by the company or payroll system.
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "The unique employee number assigned by the company or payroll system.", required = true)
    private String employeeID;

    /**
     * When the person started at the organisation. This is used by some LeaveEntitlement calculations.
     */
    @NotNull
    @ApiModelProperty(
        value = "When the person started at the organisation. This is used by some LeaveEntitlement calculations.",
        required = true
    )
    private LocalDate startDate;

    /**
     * A read-only field consisting out of the first name and last name of the person.
     */
    @Size(max = 100)
    @ApiModelProperty(value = "A read-only field consisting out of the first name and last name of the person.")
    private String name;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @Size(max = 100)
    private String email;

    /**
     * Preferably the cellphone number. This can be used for sending messages to the person.
     */
    @Size(max = 50)
    @ApiModelProperty(value = "Preferably the cellphone number. This can be used for sending messages to the person.")
    private String contractNumber;

    @NotNull
    @Size(max = 2)
    private String gender;

    /**
     * The number of annual leave days an employee is entitled to according to there employment contract,
     */
    private BigDecimal annualLeaveEntitlement;

    private UserDTO user;

    private Set<TeamDTO> teams = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getAnnualLeaveEntitlement() {
        return annualLeaveEntitlement;
    }

    public void setAnnualLeaveEntitlement(BigDecimal annualLeaveEntitlement) {
        this.annualLeaveEntitlement = annualLeaveEntitlement;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamDTO> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffDTO)) {
            return false;
        }

        StaffDTO staffDTO = (StaffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + getId() +
            ", position='" + getPosition() + "'" +
            ", employeeID='" + getEmployeeID() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", name='" + getName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", contractNumber='" + getContractNumber() + "'" +
            ", gender='" + getGender() + "'" +
            ", annualLeaveEntitlement=" + getAnnualLeaveEntitlement() +
            ", user=" + getUser() +
            ", teams=" + getTeams() +
            "}";
    }
}
