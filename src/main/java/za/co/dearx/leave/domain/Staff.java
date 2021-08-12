package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An employee of the company. The person can be linked to zero or more teams and may be linked to a User of the system.
 */
@Entity
@Table(name = "staff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = "position", length = 50)
    private String position;

    /**
     * The unique employee number assigned by the company or payroll system.
     */
    @NotNull
    @Size(max = 50)
    @Column(name = "employee_id", length = 50, nullable = false)
    private String employeeID;

    /**
     * When the person started at the organisation. This is used by some LeaveEntitlement calculations.
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    /**
     * Preferably the cellphone number. This can be used for sending messages to the person.
     */
    @Size(max = 50)
    @Column(name = "contract_number", length = 50)
    private String contractNumber;

    @NotNull
    @Size(max = 2)
    @Column(name = "gender", length = 2, nullable = false)
    private String gender;

    /**
     * The number of annual leave days an employee is entitled to according to there employment contract,\nThis should be populated using {@link EntitlementValue} table according to the linked user
     */
    @Column(name = "annual_leave_entitlement", precision = 21, scale = 2)
    private BigDecimal annualLeaveEntitlement;

    /**
     * The system user, used to access the front-end applications, linked to the person.
     */
    @ManyToOne
    private User user;

    /**
     * The team(s) the person is linked to. This is used for approvals and escalations.
     */
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_staff__team", joinColumns = @JoinColumn(name = "staff_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    @JsonIgnoreProperties(value = { "manager", "members" }, allowSetters = true)
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff id(Long id) {
        this.id = id;
        return this;
    }

    public String getPosition() {
        return this.position;
    }

    public Staff position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmployeeID() {
        return this.employeeID;
    }

    public Staff employeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Staff startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Transient
    public String getName() {
        if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) return (
            firstName + " " + lastName
        ); else return firstName + lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Staff firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Staff lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Staff email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public Staff contractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getGender() {
        return this.gender;
    }

    public Staff gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getAnnualLeaveEntitlement() {
        return this.annualLeaveEntitlement;
    }

    public Staff annualLeaveEntitlement(BigDecimal annualLeaveEntitlement) {
        this.annualLeaveEntitlement = annualLeaveEntitlement;
        return this;
    }

    public void setAnnualLeaveEntitlement(BigDecimal annualLeaveEntitlement) {
        this.annualLeaveEntitlement = annualLeaveEntitlement;
    }

    public User getUser() {
        return this.user;
    }

    public Staff user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Team> getTeams() {
        return this.teams;
    }

    public Staff teams(Set<Team> teams) {
        this.setTeams(teams);
        return this;
    }

    public Staff addTeam(Team team) {
        this.teams.add(team);
        team.getMembers().add(this);
        return this;
    }

    public Staff removeTeam(Team team) {
        this.teams.remove(team);
        team.getMembers().remove(this);
        return this;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Staff)) {
            return false;
        }
        return id != null && id.equals(((Staff) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Staff{" +
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
            "}";
    }
}
