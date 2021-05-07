package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToOne
    private User manager;

    @ManyToMany(mappedBy = "teams")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "teams" }, allowSetters = true)
    private Set<Staff> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getManager() {
        return this.manager;
    }

    public Team manager(User user) {
        this.setManager(user);
        return this;
    }

    public void setManager(User user) {
        this.manager = user;
    }

    public Set<Staff> getMembers() {
        return this.members;
    }

    public Team members(Set<Staff> staff) {
        this.setMembers(staff);
        return this;
    }

    public Team addMember(Staff staff) {
        this.members.add(staff);
        staff.getTeams().add(this);
        return this;
    }

    public Team removeMember(Staff staff) {
        this.members.remove(staff);
        staff.getTeams().remove(this);
        return this;
    }

    public void setMembers(Set<Staff> staff) {
        if (this.members != null) {
            this.members.forEach(i -> i.removeTeam(this));
        }
        if (staff != null) {
            staff.forEach(i -> i.addTeam(this));
        }
        this.members = staff;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
