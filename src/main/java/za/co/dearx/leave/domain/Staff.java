package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Staff.
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

    @NotNull
    @Size(max = 50)
    @Column(name = "employee_id", length = 50, nullable = false)
    private String employeeID;

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

    @Size(max = 50)
    @Column(name = "contract_number", length = 50)
    private String contractNumber;

    @NotNull
    @Size(max = 2)
    @Column(name = "gender", length = 2, nullable = false)
    private String gender;

    @ManyToOne
    @JsonIgnoreProperties(value = "staff", allowSetters = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "staff_team",
        joinColumns = @JoinColumn(name = "staff_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id")
    )
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public Staff position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public Staff employeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getStartDate() {
        return startDate;
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
        return firstName;
    }

    public Staff firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Staff lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Staff email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Staff contractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getGender() {
        return gender;
    }

    public Staff gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User getUser() {
        return user;
    }

    public Staff user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public Staff teams(Set<Team> teams) {
        this.teams = teams;
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
        return 31;
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
            "}";
    }
}
